package com.eyther.lumbridge.features.editloan.model

import com.eyther.lumbridge.model.loan.LoanCategoryUi
import com.eyther.lumbridge.ui.common.composables.model.input.ChoiceTabState
import com.eyther.lumbridge.ui.common.composables.model.input.DateInputState
import com.eyther.lumbridge.ui.common.composables.model.input.TextInputState

data class EditLoanScreenInputState(
    val name: TextInputState = TextInputState(),
    val initialAmount: TextInputState = TextInputState(),
    val currentAmount: TextInputState = TextInputState(),
    val startDate: DateInputState = DateInputState(),
    val endDate: DateInputState = DateInputState(),
    val euribor: TextInputState = TextInputState(),
    val spread: TextInputState = TextInputState(),
    val tanInterestRate: TextInputState = TextInputState(),
    val taegInterestRate: TextInputState = TextInputState(),
    val categoryUi: LoanCategoryUi = LoanCategoryUi.House,
    val fixedOrVariableLoanChoiceState: ChoiceTabState = ChoiceTabState(),
    val tanOrTaegLoanChoiceState: ChoiceTabState = ChoiceTabState(),
    val shouldNotifyWhenPaid: Boolean = false,
    val shouldAutoAddToExpenses: Boolean = false,
    val paymentDay: TextInputState = TextInputState()
)
