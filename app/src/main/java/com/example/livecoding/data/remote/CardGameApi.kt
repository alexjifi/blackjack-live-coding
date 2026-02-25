package com.example.livecoding.data.remote

import com.example.livecoding.data.remote.dto.DrawCardsResponseDto
import com.example.livecoding.data.remote.dto.NewDeckResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CardGameApi {
    @GET("api/deck/new/shuffle/")
    suspend fun createNewDeck(
        @Query("deck_count") deckCount: Int = 1
    ): NewDeckResponseDto

    @GET("api/deck/{deckId}/draw/")
    suspend fun drawCards(
        @Path("deckId") deckId: String,
        @Query("count") count: Int
    ): DrawCardsResponseDto
}
