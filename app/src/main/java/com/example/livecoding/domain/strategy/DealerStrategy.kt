package com.example.livecoding.domain.strategy

import com.example.livecoding.domain.model.Card

interface DealerStrategy {
    fun shouldHit(dealerHand: List<Card>, dealerScore: Int, playerScore: Int): Boolean
}
