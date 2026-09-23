package com.example.tipsplitter

import java.math.BigDecimal
import java.math.RoundingMode

data class Split(
    val tip: BigDecimal,
    val total: BigDecimal,
    val perPerson: BigDecimal,
)

object TipCalculator {
    fun split(bill: BigDecimal, tipPercent: Int, people: Int): Split {
        require(bill >= BigDecimal.ZERO) { "Bill cannot be negative" }
        require(tipPercent >= 0) { "Tip cannot be negative" }
        require(people >= 1) { "At least one person has to pay" }

        val tip = bill.multiply(BigDecimal(tipPercent)).divide(BigDecimal(100), 2, RoundingMode.HALF_UP)
        val total = bill.add(tip).setScale(2, RoundingMode.HALF_UP)
        // Round up so the group never pays less than the total.
        val perPerson = total.divide(BigDecimal(people), 2, RoundingMode.CEILING)
        return Split(tip, total, perPerson)
    }
}
