package com.example.livecoding.domain.model

object HandScoreCalculator {

    const val BLACKJACK_SCORE = 21

    fun calculateScore(cards: List<Card>): Int {
        var score = 0
        var aces = 0

        cards.forEach { card ->
            when (card.value) {
                "ACE" -> {
                    score += 11
                    aces += 1
                }
                "KING", "QUEEN", "JACK", "10" -> score += 10
                else -> score += card.value.toIntOrNull() ?: 0
            }
        }

        while (score > BLACKJACK_SCORE && aces > 0) {
            score -= 10
            aces--
        }

        return score
    }

    fun isSoft(cards: List<Card>): Boolean {
        var score = 0
        var aces = 0

        cards.forEach { card ->
            when (card.value) {
                "ACE" -> {
                    score += 11
                    aces += 1
                }
                "KING", "QUEEN", "JACK", "10" -> score += 10
                else -> score += card.value.toIntOrNull() ?: 0
            }
        }

        while (score > BLACKJACK_SCORE && aces > 1) {
            score -= 10
            aces--
        }

        return aces >= 1 && score <= BLACKJACK_SCORE
    }
}
