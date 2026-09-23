package com.example.tipsplitter

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class TipCalculatorTest {

    @Test
    fun `adds the tip to the bill`() {
        val split = TipCalculator.split(BigDecimal("100.00"), tipPercent = 15, people = 1)

        assertEquals(BigDecimal("15.00"), split.tip)
        assertEquals(BigDecimal("115.00"), split.total)
        assertEquals(BigDecimal("115.00"), split.perPerson)
    }

    @Test
    fun `rounds each share up so the total is covered`() {
        val split = TipCalculator.split(BigDecimal("100.00"), tipPercent = 0, people = 3)

        assertEquals(BigDecimal("33.34"), split.perPerson)
    }

    @Test
    fun `zero bill costs nothing`() {
        val split = TipCalculator.split(BigDecimal.ZERO, tipPercent = 20, people = 4)

        assertEquals(BigDecimal("0.00"), split.perPerson)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `rejects zero people`() {
        TipCalculator.split(BigDecimal("10"), tipPercent = 10, people = 0)
    }
}
