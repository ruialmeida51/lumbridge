package com.eyther.lumbridge.domain.mapper.recurringpayments

import com.eyther.lumbridge.data.model.recurringpayments.local.RecurringPaymentCached
import com.eyther.lumbridge.domain.model.expenses.ExpensesCategoryTypes
import com.eyther.lumbridge.domain.model.netsalary.allocation.MoneyAllocationType
import com.eyther.lumbridge.domain.model.recurringpayments.RecurringPaymentDomain
import com.eyther.lumbridge.shared.time.extensions.toLocalDate

fun RecurringPaymentCached.toDomain() = RecurringPaymentDomain(
    id = id,
    label = label,
    startDate = startDate.toLocalDate(),
    lastPaymentDate = lastPaymentDate?.toLocalDate(),
    periodicity = periodicity,
    shouldNotifyWhenPaid = shouldNotifyWhenPaid,
    amountToPay = amountToPay,
    categoryTypes = ExpensesCategoryTypes.of(categoryTypeOrdinal),
    allocationType = MoneyAllocationType.of(allocationTypeOrdinal),
)

fun List<RecurringPaymentCached>.toDomain() = map { it.toDomain() }
