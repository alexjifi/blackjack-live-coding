package com.example.livecoding.domain.usecase

import com.example.livecoding.domain.model.Card
import com.example.livecoding.domain.repository.CardGameRepository

class DrawOpeningHandUseCase(
    private val repository: CardGameRepository
) {
    suspend operator fun invoke(cardCount: Int = DEFAULT_HAND_SIZE): Result<List<Card>> = runCatching {
        require(cardCount in 1..MAX_HAND_SIZE) {
            "cardCount must be between 1 and $MAX_HAND_SIZE"
        }
        repository.drawOpeningHand(cardCount)
    }

    private companion object {
        private const val MAX_HAND_SIZE = 10
        private const val DEFAULT_HAND_SIZE = 5
    }
}
