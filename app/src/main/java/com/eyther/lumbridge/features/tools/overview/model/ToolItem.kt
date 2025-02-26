package com.eyther.lumbridge.features.tools.overview.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.eyther.lumbridge.R

sealed class ToolItem(
    @StringRes val text: Int,
    @DrawableRes val icon: Int
) {
    companion object {
        fun getItems() = mapOf(
            R.string.tools_lifestyle_tools to listOf(
                Lifestyle.ShoppingList,
                Lifestyle.Notes,
                Lifestyle.Reminders,
                Lifestyle.RecurringPayments
            ),
            R.string.tools_finance_resources_tools to listOf(
                Personal.NetSalaryCalculator,
                Personal.CurrencyConverter
            )
        )
    }

    sealed interface Lifestyle {
        data object ShoppingList : ToolItem(
            text = R.string.tools_shopping_list,
            icon = R.drawable.ic_basket
        )

        data object Notes : ToolItem(
            text = R.string.tools_notes_list,
            icon = R.drawable.ic_sticky_note
        )
        
        data object Reminders : ToolItem(
            text = R.string.tools_reminders_list,
            icon = R.drawable.ic_checklist
        )

        data object RecurringPayments : ToolItem(
            text = R.string.recurring_payments_overview,
            icon = R.drawable.ic_time
        )
    }

    sealed interface Personal {
        data object NetSalaryCalculator : ToolItem(
            text = R.string.tools_net_salary_calculator,
            icon = R.drawable.ic_balance
        )

        data object CurrencyConverter : ToolItem(
            text = R.string.tools_currency_converter,
            icon = R.drawable.ic_currency_exchange
        )
    }
}
