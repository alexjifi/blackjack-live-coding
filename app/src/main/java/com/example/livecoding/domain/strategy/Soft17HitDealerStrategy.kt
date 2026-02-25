package com.example.livecoding.domain.strategy

import com.example.livecoding.domain.model.Card
import com.example.livecoding.domain.model.HandScoreCalculator

class Soft17HitDealerStrategy : DealerStrategy {

    override fun shouldHit(dealerHand: List<Card>, dealerScore: Int, playerScore: Int): Boolean {
        if (dealerScore > HandScoreCalculator.BLACKJACK_SCORE) return false
        if (dealerScore < DEALER_MIN_SCORE) return true
        if (dealerScore == DEALER_MIN_SCORE && HandScoreCalculator.isSoft(dealerHand)) return true
        return dealerScore < playerScore && dealerScore < HandScoreCalculator.BLACKJACK_SCORE
    }

    private companion object {
        private const val DEALER_MIN_SCORE = 17
    }
}
