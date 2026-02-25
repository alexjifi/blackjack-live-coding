package com.example.livecoding.domain.repository

import com.example.livecoding.domain.model.Card

interface CardGameRepository {
    suspend fun drawOpeningHand(cardCount: Int): List<Card>
}
