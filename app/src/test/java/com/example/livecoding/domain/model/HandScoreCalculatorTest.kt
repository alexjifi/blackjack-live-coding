package com.example.livecoding.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HandScoreCalculatorTest {

    private fun card(value: String) = Card(
        code = "${value[0]}S",
        value = value,
        suit = "SPADES",
        imageUrl = ""
    )

    @Test
    fun calculateScore_numericCards_returnsFaceValue() {
        val cards = listOf(card("5"), card("3"))
        assertEquals(8, HandScoreCalculator.calculateScore(cards))
    }

    @Test
    fun calculateScore_faceCards_returnsTenEach() {
        val cards = listOf(card("KING"), card("QUEEN"), card("JACK"))
        assertEquals(30, HandScoreCalculator.calculateScore(cards))
    }

    @Test
    fun calculateScore_tenCard_returnsTen() {
        val cards = listOf(card("10"), card("7"))
        assertEquals(17, HandScoreCalculator.calculateScore(cards))
    }

    @Test
    fun calculateScore_aceAsEleven() {
        val cards = listOf(card("ACE"), card("9"))
        assertEquals(20, HandScoreCalculator.calculateScore(cards))
    }

    @Test
    fun calculateScore_aceAsOne_whenBustOtherwise() {
        val cards = listOf(card("ACE"), card("9"), card("5"))
        assertEquals(15, HandScoreCalculator.calculateScore(cards))
    }

    @Test
    fun calculateScore_blackjack() {
        val cards = listOf(card("ACE"), card("KING"))
        assertEquals(21, HandScoreCalculator.calculateScore(cards))
    }

    @Test
    fun calculateScore_twoAces() {
        val cards = listOf(card("ACE"), card("ACE"))
        assertEquals(12, HandScoreCalculator.calculateScore(cards))
    }

    @Test
    fun calculateScore_emptyHand_returnsZero() {
        assertEquals(0, HandScoreCalculator.calculateScore(emptyList()))
    }

    @Test
    fun isSoft_aceCountedAsEleven_returnsTrue() {
        val cards = listOf(card("ACE"), card("6"))
        assertTrue(HandScoreCalculator.isSoft(cards))
    }

    @Test
    fun isSoft_aceCountedAsOne_returnsFalse() {
        val cards = listOf(card("ACE"), card("9"), card("5"))
        assertFalse(HandScoreCalculator.isSoft(cards))
    }

    @Test
    fun isSoft_noAces_returnsFalse() {
        val cards = listOf(card("KING"), card("7"))
        assertFalse(HandScoreCalculator.isSoft(cards))
    }

    @Test
    fun isSoft_blackjack_returnsTrue() {
        val cards = listOf(card("ACE"), card("KING"))
        assertTrue(HandScoreCalculator.isSoft(cards))
    }

    @Test
    fun isSoft_twoAces_returnsTrue() {
        val cards = listOf(card("ACE"), card("ACE"))
        assertTrue(HandScoreCalculator.isSoft(cards))
    }
}
