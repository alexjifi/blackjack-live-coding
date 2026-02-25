package com.example.livecoding.data.repository

import com.example.livecoding.data.remote.CardGameRemoteDataSource
import com.example.livecoding.data.remote.dto.CardDto
import com.example.livecoding.domain.model.Card
import com.example.livecoding.domain.repository.CardGameRepository

class CardGameRepositoryImpl(
    private val remoteDataSource: CardGameRemoteDataSource
) : CardGameRepository {

    private var cachedDeckId: String? = null

    override suspend fun drawOpeningHand(cardCount: Int): List<Card> {
        ensureDeckAvailable()
        val deckId = cachedDeckId ?: error("Deck is not available")
        val drawResponse = remoteDataSource.drawCards(deckId = deckId, count = cardCount)

        if (!drawResponse.success) {
            error("The API could not draw cards")
        }

        cachedDeckId = drawResponse.deckId
        return drawResponse.cards.map(CardDto::toDomain)
    }

    private suspend fun ensureDeckAvailable() {
        if (cachedDeckId != null) return

        val newDeck = remoteDataSource.createNewDeck()
        if (!newDeck.success) {
            error("The API could not create a new deck")
        }

        cachedDeckId = newDeck.deckId
    }
}

private fun CardDto.toDomain(): Card = Card(
    code = code,
    value = value,
    suit = suit,
    imageUrl = image
)
