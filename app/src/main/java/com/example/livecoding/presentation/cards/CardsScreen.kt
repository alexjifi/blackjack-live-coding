package com.example.livecoding.presentation.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.livecoding.domain.model.Card as DomainCard
import com.example.livecoding.ui.theme.LiveCodingTheme

@Composable
fun CardsRoute(viewModel: CardsViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CardsScreen(
        uiState = uiState,
        onHit = viewModel::onHitClicked,
        onStand = viewModel::onStandClicked,
        onNewRound = viewModel::onNewRoundClicked,
        onRetry = viewModel::onRetryClicked
    )
}

@Composable
fun CardsScreen(
    uiState: CardsUiState,
    onHit: () -> Unit,
    onStand: () -> Unit,
    onNewRound: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Blackjack",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Objetivo: acercarte a 21 sin pasarte. El crupier juega al plantarte.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = uiState.statusMessage,
                style = MaterialTheme.typography.titleMedium
            )

            DealerSection(uiState = uiState)
            PlayerSection(uiState = uiState)

            GameActions(
                uiState = uiState,
                onHit = onHit,
                onStand = onStand,
                onNewRound = onNewRound
            )

            if (uiState.isLoading) {
                LoadingState()
            }

            uiState.errorMessage?.let { message ->
                ErrorState(
                    message = message,
                    onRetry = onRetry
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DealerSection(uiState: CardsUiState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val visibleDealerScore = if (uiState.isDealerSecondCardHidden && uiState.dealerCards.size >= 2) {
                calculateVisibleScore(uiState.dealerCards.first())
            } else {
                uiState.dealerScore
            }
            Text(
                text = "Crupier · Puntuación: $visibleDealerScore",
                style = MaterialTheme.typography.titleMedium
            )
            CardsRow(
                cards = uiState.dealerCards,
                hideSecondCard = uiState.isDealerSecondCardHidden
            )
        }
    }
}

@Composable
private fun PlayerSection(uiState: CardsUiState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Jugador · Puntuación: ${uiState.playerScore}",
                style = MaterialTheme.typography.titleMedium
            )
            CardsRow(cards = uiState.playerCards)
        }
    }
}

@Composable
private fun GameActions(
    uiState: CardsUiState,
    onHit: () -> Unit,
    onStand: () -> Unit,
    onNewRound: () -> Unit
) {
    val playerTurn = uiState.gameStatus == GameStatus.PLAYER_TURN
    val canPlay = playerTurn && !uiState.isLoading && uiState.errorMessage == null

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onHit, enabled = canPlay) {
                Text("Pedir")
            }
            Button(onClick = onStand, enabled = canPlay) {
                Text("Plantarse")
            }
            OutlinedButton(onClick = onNewRound, enabled = !uiState.isLoading) {
                Text("Nueva ronda")
            }
        }
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "No se pudo continuar la partida",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
        Text(text = message, style = MaterialTheme.typography.bodyMedium)
        OutlinedButton(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}

@Composable
private fun CardsRow(
    cards: List<DomainCard>,
    hideSecondCard: Boolean = false
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(cards.size) { index ->
            val card = cards[index]
            val shouldHideCard = hideSecondCard && index == 1
            val imageUrl = if (shouldHideCard) HIDDEN_CARD_IMAGE_URL else card.imageUrl
            val description = if (shouldHideCard) "Carta oculta del crupier" else "Carta ${card.code}"

            AsyncImage(
                model = imageUrl,
                contentDescription = description
            )
        }
    }
}

private fun calculateVisibleScore(card: DomainCard): Int = when (card.value) {
    "ACE" -> 11
    "KING", "QUEEN", "JACK", "10" -> 10
    else -> card.value.toIntOrNull() ?: 0
}

private const val HIDDEN_CARD_IMAGE_URL = "https://deckofcardsapi.com/static/img/back.png"

@Preview(showBackground = true)
@Composable
private fun CardsScreenPreview() {
    LiveCodingTheme {
        CardsScreen(
            uiState = CardsUiState(
                playerCards = listOf(
                    DomainCard(
                        code = "AS",
                        value = "ACE",
                        suit = "SPADES",
                        imageUrl = "https://deckofcardsapi.com/static/img/AS.png"
                    ),
                    DomainCard(
                        code = "QH",
                        value = "QUEEN",
                        suit = "HEARTS",
                        imageUrl = "https://deckofcardsapi.com/static/img/QH.png"
                    )
                ),
                dealerCards = listOf(
                    DomainCard(
                        code = "8D",
                        value = "8",
                        suit = "DIAMONDS",
                        imageUrl = "https://deckofcardsapi.com/static/img/8D.png"
                    ),
                    DomainCard(
                        code = "9C",
                        value = "9",
                        suit = "CLUBS",
                        imageUrl = "https://deckofcardsapi.com/static/img/9C.png"
                    )
                ),
                playerScore = 21,
                dealerScore = 17,
                statusMessage = "Tu turno"
            ),
            onHit = {},
            onStand = {},
            onNewRound = {},
            onRetry = {}
        )
    }
}
