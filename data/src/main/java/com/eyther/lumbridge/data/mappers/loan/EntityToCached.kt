package com.eyther.lumbridge.data.mappers.loan

import com.eyther.lumbridge.data.model.loan.entity.LoanEntity
import com.eyther.lumbridge.data.model.loan.local.LoanCached

fun LoanEntity.toCached() = LoanCached(
    id = loanId,
    name = name,
    startDate = startDate,
    currentPaymentDate = currentPaymentDate,
    endDate = endDate,
    currentAmount = currentAmount,
    initialAmount = initialAmount,
    fixedTaegInterestRate = fixedTaegInterestRate,
    variableEuribor = variableEuribor,
    variableSpread = variableSpread,
    fixedTanInterestRate = fixedTanInterestRate,
    loanTypeOrdinal = loanTypeOrdinal,
    loanCategoryOrdinal = loanCategoryOrdinal,
    shouldNotifyWhenPaid = shouldNotifyWhenPaid,
    shouldAutoAddToExpenses = shouldAutoAddToExpenses,
    lastAutoPayDate = lastAutoPayDate,
    paymentDay = paymentDay
)
