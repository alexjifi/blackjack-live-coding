package com.example.livecoding.presentation.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.livecoding.domain.model.Card
import com.example.livecoding.domain.usecase.DrawOpeningHandUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CardsViewModel(
    private val drawOpeningHandUseCase: DrawOpeningHandUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardsUiState(isLoading = true))
    val uiState: StateFlow<CardsUiState> = _uiState.asStateFlow()

    init {
        startNewRound()
    }

    fun onNewRoundClicked() {
        startNewRound()
    }

    fun onRetryClicked() {
        startNewRound()
    }

    fun onHitClicked() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState.isLoading || currentState.gameStatus != GameStatus.PLAYER_TURN) return@launch

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            drawOpeningHandUseCase(cardCount = 1).fold(
                onSuccess = { cards ->
                    val newCard = cards.first()
                    val updatedPlayerCards = currentState.playerCards + newCard
                    val playerScore = calculateHandScore(updatedPlayerCards)

                    if (playerScore > BLACKJACK_SCORE) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                playerCards = updatedPlayerCards,
                                playerScore = playerScore,
                                isDealerSecondCardHidden = false,
                                gameStatus = GameStatus.FINISHED,
                                statusMessage = "Te pasaste de 21. Gana el crupier."
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                playerCards = updatedPlayerCards,
                                playerScore = playerScore,
                                statusMessage = "Tu turno"
                            )
                        }
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "No se pudo pedir carta."
                        )
                    }
                }
            )
        }
    }

    fun onStandClicked() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState.isLoading || currentState.gameStatus != GameStatus.PLAYER_TURN) return@launch

            _uiState.update {
                it.copy(
                    isLoading = true,
                    isDealerSecondCardHidden = false,
                    gameStatus = GameStatus.DEALER_TURN,
                    statusMessage = "Turno del crupier..."
                )
            }

            playDealerTurn(playerScore = currentState.playerScore)
        }
    }

    private fun startNewRound() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    errorMessage = null,
                    statusMessage = "Repartiendo cartas..."
                )
            }

            drawOpeningHandUseCase(cardCount = INITIAL_DRAW_COUNT).fold(
                onSuccess = { cards ->
                    val playerCards = listOf(cards[0], cards[2])
                    val dealerCards = listOf(cards[1], cards[3])
                    val playerScore = calculateHandScore(playerCards)
                    val dealerScore = calculateHandScore(dealerCards)

                    val (status, message, hiddenDealerCard) = when {
                        playerScore == BLACKJACK_SCORE && dealerScore == BLACKJACK_SCORE ->
                            Triple(GameStatus.FINISHED, "Empate. Ambos tienen Blackjack.", false)

                        playerScore == BLACKJACK_SCORE ->
                            Triple(GameStatus.FINISHED, "Blackjack! Ganas la ronda.", false)

                        dealerScore == BLACKJACK_SCORE ->
                            Triple(GameStatus.FINISHED, "El crupier tiene Blackjack.", false)

                        else ->
                            Triple(GameStatus.PLAYER_TURN, "Tu turno", true)
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            playerCards = playerCards,
                            dealerCards = dealerCards,
                            playerScore = playerScore,
                            dealerScore = dealerScore,
                            isDealerSecondCardHidden = hiddenDealerCard,
                            gameStatus = status,
                            statusMessage = message,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            playerCards = emptyList(),
                            dealerCards = emptyList(),
                            errorMessage = throwable.message ?: "No se pudo iniciar la partida."
                        )
                    }
                }
            )
        }
    }

    private suspend fun playDealerTurn(playerScore: Int) {
        var dealerCards = _uiState.value.dealerCards
        var dealerScore = calculateHandScore(dealerCards)

        while (shouldDealerHit(dealerScore = dealerScore, playerScore = playerScore)) {
            val drawResult = drawOpeningHandUseCase(cardCount = 1)
            val drawnCard = drawResult.getOrElse { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        gameStatus = GameStatus.FINISHED,
                        errorMessage = throwable.message ?: "No se pudo completar el turno del crupier."
                    )
                }
                return
            }.first()

            dealerCards = dealerCards + drawnCard
            dealerScore = calculateHandScore(dealerCards)

            _uiState.update {
                it.copy(
                    dealerCards = dealerCards,
                    dealerScore = dealerScore
                )
            }
        }

        val finalMessage = resolveRoundResult(playerScore = playerScore, dealerScore = dealerScore)
        _uiState.update {
            it.copy(
                isLoading = false,
                dealerCards = dealerCards,
                dealerScore = dealerScore,
                gameStatus = GameStatus.FINISHED,
                statusMessage = finalMessage
            )
        }
    }

    private fun shouldDealerHit(dealerScore: Int, playerScore: Int): Boolean {
        if (dealerScore > BLACKJACK_SCORE) return false
        if (dealerScore < DEALER_MIN_SCORE) return true
        return dealerScore < playerScore && dealerScore < BLACKJACK_SCORE
    }

    private fun resolveRoundResult(playerScore: Int, dealerScore: Int): String = when {
        dealerScore > BLACKJACK_SCORE -> "El crupier se pasó. Ganas la ronda."
        dealerScore >= playerScore -> "El crupier gana la ronda."
        else -> "Ganaste la ronda."
    }

    private fun calculateHandScore(cards: List<Card>): Int {
        var score = 0
        var aces = 0

        cards.forEach { card ->
            when (card.value) {
                "ACE" -> {
                    score += 11
                    aces += 1
                }

                "KING", "QUEEN", "JACK", "10" -> score += 10
                else -> score += card.value.toIntOrNull() ?: 0
            }
        }

        while (score > BLACKJACK_SCORE && aces > 0) {
            score -= 10
            aces--
        }

        return score
    }

    private companion object {
        private const val INITIAL_DRAW_COUNT = 4
        private const val BLACKJACK_SCORE = 21
        private const val DEALER_MIN_SCORE = 17
    }
}

class CardsViewModelFactory(
    private val drawOpeningHandUseCase: DrawOpeningHandUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsViewModel::class.java)) {
            return CardsViewModel(drawOpeningHandUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
