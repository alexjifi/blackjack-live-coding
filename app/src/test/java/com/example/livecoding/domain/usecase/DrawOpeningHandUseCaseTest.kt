package com.example.livecoding.domain.usecase

import com.example.livecoding.domain.model.Card
import com.example.livecoding.domain.repository.CardGameRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DrawOpeningHandUseCaseTest {

    @Test
    fun invoke_returnsCardsFromRepository() = runBlocking {
        val expectedCards = listOf(
            Card(
                code = "AS",
                value = "ACE",
                suit = "SPADES",
                imageUrl = "https://deckofcardsapi.com/static/img/AS.png"
            ),
            Card(
                code = "KH",
                value = "KING",
                suit = "HEARTS",
                imageUrl = "https://deckofcardsapi.com/static/img/KH.png"
            )
        )
        val fakeRepository = FakeCardGameRepository(cardsToReturn = expectedCards)
        val useCase = DrawOpeningHandUseCase(repository = fakeRepository)

        val result = useCase(cardCount = 2)

        assertTrue(result.isSuccess)
        assertEquals(expectedCards, result.getOrNull())
        assertEquals(2, fakeRepository.lastRequestedCount)
    }
}

private class FakeCardGameRepository(
    private val cardsToReturn: List<Card>
) : CardGameRepository {

    var lastRequestedCount: Int = 0
        private set

    override suspend fun drawOpeningHand(cardCount: Int): List<Card> {
        lastRequestedCount = cardCount
        return cardsToReturn
    }
}
