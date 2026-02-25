package com.example.livecoding.domain.strategy

import com.example.livecoding.domain.model.Card
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DealerStrategyTest {

    private fun card(value: String) = Card(
        code = "${value[0]}S",
        value = value,
        suit = "SPADES",
        imageUrl = ""
    )

    // --- StandardDealerStrategy ---

    private val standard = StandardDealerStrategy()

    @Test
    fun standard_hitsBelow17() {
        val hand = listOf(card("10"), card("5"))
        assertTrue(standard.shouldHit(hand, dealerScore = 15, playerScore = 18))
    }

    @Test
    fun standard_standsOn17WhenWinning() {
        val hand = listOf(card("10"), card("7"))
        assertFalse(standard.shouldHit(hand, dealerScore = 17, playerScore = 16))
    }

    @Test
    fun standard_standsOnSoft17WhenWinning() {
        val hand = listOf(card("ACE"), card("6"))
        assertFalse(standard.shouldHit(hand, dealerScore = 17, playerScore = 16))
    }

    @Test
    fun standard_hitsOn17WhenLosing() {
        val hand = listOf(card("10"), card("7"))
        assertTrue(standard.shouldHit(hand, dealerScore = 17, playerScore = 19))
    }

    @Test
    fun standard_stopsWhenBust() {
        val hand = listOf(card("10"), card("KING"), card("5"))
        assertFalse(standard.shouldHit(hand, dealerScore = 25, playerScore = 19))
    }

    @Test
    fun standard_standsOn21() {
        val hand = listOf(card("ACE"), card("KING"))
        assertFalse(standard.shouldHit(hand, dealerScore = 21, playerScore = 20))
    }

    // --- Soft17HitDealerStrategy ---

    private val soft17 = Soft17HitDealerStrategy()

    @Test
    fun soft17_hitsOnSoft17() {
        val hand = listOf(card("ACE"), card("6"))
        assertTrue(soft17.shouldHit(hand, dealerScore = 17, playerScore = 16))
    }

    @Test
    fun soft17_standsOnHard17WhenTied() {
        val hand = listOf(card("10"), card("7"))
        assertFalse(soft17.shouldHit(hand, dealerScore = 17, playerScore = 17))
    }

    @Test
    fun soft17_standsOnSoft18() {
        val hand = listOf(card("ACE"), card("7"))
        assertFalse(soft17.shouldHit(hand, dealerScore = 18, playerScore = 17))
    }

    @Test
    fun soft17_hitsBelow17() {
        val hand = listOf(card("10"), card("4"))
        assertTrue(soft17.shouldHit(hand, dealerScore = 14, playerScore = 20))
    }

    @Test
    fun soft17_stopsWhenBust() {
        val hand = listOf(card("10"), card("KING"), card("3"))
        assertFalse(soft17.shouldHit(hand, dealerScore = 23, playerScore = 18))
    }

    @Test
    fun soft17_hitsOnHard17WhenLosing() {
        val hand = listOf(card("10"), card("7"))
        assertTrue(soft17.shouldHit(hand, dealerScore = 17, playerScore = 19))
    }
}
