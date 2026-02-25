package com.example.livecoding.presentation.cards

import com.example.livecoding.domain.model.Card

data class CardsUiState(
    val isLoading: Boolean = false,
    val playerCards: List<Card> = emptyList(),
    val dealerCards: List<Card> = emptyList(),
    val isDealerSecondCardHidden: Boolean = true,
    val playerScore: Int = 0,
    val dealerScore: Int = 0,
    val gameStatus: GameStatus = GameStatus.PLAYER_TURN,
    val statusMessage: String = "Tu turno",
    val errorMessage: String? = null
)

enum class GameStatus {
    PLAYER_TURN,
    DEALER_TURN,
    FINISHED
}
