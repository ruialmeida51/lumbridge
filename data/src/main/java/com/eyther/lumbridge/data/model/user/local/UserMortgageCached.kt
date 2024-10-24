package com.eyther.lumbridge.data.model.user.local

data class UserMortgageCached(
    val loanType: String,
    val loanAmount: Float,
    val euribor: Float?,
    val spread: Float?,
    val fixedInterestRate: Float?,
    val startDate: String,
    val endDate: String
)
