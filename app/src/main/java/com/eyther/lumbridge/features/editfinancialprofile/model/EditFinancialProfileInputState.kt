package com.eyther.lumbridge.features.editfinancialprofile.model

import com.eyther.lumbridge.ui.common.composables.model.TextInputState

data class EditFinancialProfileInputState(
    val annualGrossSalary: TextInputState = TextInputState(),
    val foodCardPerDiem: TextInputState = TextInputState(),
    val savingsPercentage: TextInputState = TextInputState(),
    val necessitiesPercentage: TextInputState = TextInputState(),
    val luxuriesPercentage: TextInputState = TextInputState(),
    val numberOfDependants: TextInputState = TextInputState(),
    val singleIncome: Boolean = false,
    val married: Boolean = false,
    val handicapped: Boolean = false
)
