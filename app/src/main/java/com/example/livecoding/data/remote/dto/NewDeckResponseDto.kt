package com.example.livecoding.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NewDeckResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("deck_id") val deckId: String,
    @SerializedName("remaining") val remaining: Int
)
