package com.eyther.lumbridge.domain.model.loan

import com.eyther.lumbridge.shared.time.monthsUntil
import java.time.LocalDate

data class Loan(
    val id: Long = -1,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val initialAmount: Float,
    val currentAmount: Float,
    val loanInterestRate: LoanInterestRate,
    val loanType: LoanType,
    val loanCategory: LoanCategory
) {
    val remainingMonths: Int
        get() = startDate.monthsUntil(endDate)
}
