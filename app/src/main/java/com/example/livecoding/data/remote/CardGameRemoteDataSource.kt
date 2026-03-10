package com.example.livecoding.data.remote

import com.example.livecoding.data.remote.dto.DrawCardsResponseDto
import com.example.livecoding.data.remote.dto.NewDeckResponseDto

class CardGameRemoteDataSource(
    private val api: CardGameApi
) {
    suspend fun createNewDeck(): NewDeckResponseDto = api.createNewDeck()

    suspend fun drawCards(deckId: String, count: Int): DrawCardsResponseDto {
        Thread.sleep(3000)
        return api.drawCards(
            deckId = deckId,
            count = count
        )
    }
}
