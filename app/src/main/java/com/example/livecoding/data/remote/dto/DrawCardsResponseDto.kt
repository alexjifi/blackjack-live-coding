package com.example.livecoding.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DrawCardsResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("deck_id") val deckId: String,
    @SerializedName("remaining") val remaining: Int,
    @SerializedName("cards") val cards: List<CardDto>
)

data class CardDto(
    @SerializedName("code") val code: String,
    @SerializedName("image") val image: String,
    @SerializedName("value") val value: String,
    @SerializedName("suit") val suit: String
)
