package com.eyther.lumbridge.domain.model.preferences

import com.eyther.lumbridge.domain.model.locale.SupportedLanguages

data class Preferences(
    val isDarkMode: Boolean,
    val appLanguage: SupportedLanguages,
    val showAllocationsOnExpenses: Boolean,
    val addFoodCardToNecessitiesAllocation: Boolean
)
