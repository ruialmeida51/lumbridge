@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.eyther.lumbridge.features.editmortgageprofile.screens

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.eyther.lumbridge.R
import com.eyther.lumbridge.domain.time.toLocalDate
import com.eyther.lumbridge.features.editmortgageprofile.model.EditMortgageProfileScreenViewEffect
import com.eyther.lumbridge.features.editmortgageprofile.model.EditMortgageProfileScreenViewEffect.None
import com.eyther.lumbridge.features.editmortgageprofile.model.EditMortgageProfileScreenViewState
import com.eyther.lumbridge.features.editmortgageprofile.viewmodel.EditMortgageProfileScreenViewModel
import com.eyther.lumbridge.features.editmortgageprofile.viewmodel.IEditMortgageProfileScreenViewModel
import com.eyther.lumbridge.model.mortgage.MortgageTypeUi
import com.eyther.lumbridge.ui.common.composables.components.buttons.LumbridgeButton
import com.eyther.lumbridge.ui.common.composables.components.datepicker.LumbridgeDatePickerDialog
import com.eyther.lumbridge.ui.common.composables.components.input.DateInput
import com.eyther.lumbridge.ui.common.composables.components.input.NumberInput
import com.eyther.lumbridge.ui.common.composables.components.loading.LoadingIndicator
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.LumbridgeTopAppBar
import com.eyther.lumbridge.ui.common.composables.components.topAppBar.TopAppBarVariation
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.HalfPadding
import com.eyther.lumbridge.ui.theme.QuarterPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography
import java.time.LocalDate

@Composable
fun EditMortgageProfileScreen(
    navController: NavHostController,
    @StringRes label: Int,
    viewModel: IEditMortgageProfileScreenViewModel = hiltViewModel<EditMortgageProfileScreenViewModel>()
) {
    val state = viewModel.viewState.collectAsStateWithLifecycle().value
    val sideEffects = viewModel.viewEffect.collectAsStateWithLifecycle(None).value

    Scaffold(
        topBar = {
            LumbridgeTopAppBar(
                topAppBarVariation = TopAppBarVariation.TitleAndIcon(
                    title = stringResource(id = label)
                ) {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            when (sideEffects) {
                is EditMortgageProfileScreenViewEffect.ShowError -> Snackbar {
                    Text(text = sideEffects.message)
                }

                else -> Unit
            }

            when (state) {
                is EditMortgageProfileScreenViewState.Content -> Content(
                    navController = navController,
                    state = state,
                    viewModel = viewModel
                )

                is EditMortgageProfileScreenViewState.Loading -> LoadingIndicator()
            }
        }
    }
}

@Composable
fun Content(
    navController: NavHostController,
    state: EditMortgageProfileScreenViewState.Content,
    viewModel: IEditMortgageProfileScreenViewModel
) {
    Column(
        Modifier
            .padding(DefaultPadding)
            .animateContentSize()
    ) {
        RemainingAmount(
            state = state,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        MortgageType(
            state = state,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.height(DefaultPadding))

        LumbridgeButton(
            label = stringResource(id = R.string.edit_mortgage_profile_save),
            enableButton = state.shouldEnableSaveButton,
            onClick = { viewModel.saveMortgageProfile(navController) }
        )
    }
}

@Composable
private fun ColumnScope.RemainingAmount(
    state: EditMortgageProfileScreenViewState.Content,
    viewModel: IEditMortgageProfileScreenViewModel
) {
    val selectableYears = LocalDate.now().year..viewModel.getMaxSelectableYear()
    val isSelectableYear = { year: Int -> year >= LocalDate.now().year }

    val showStartDateDialog = remember { mutableStateOf(false) }
    val startDatePickerState = rememberDatePickerState(
        yearRange = selectableYears,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis.toLocalDate() >= LocalDate.now()
            }

            override fun isSelectableYear(year: Int): Boolean {
                return isSelectableYear(year)
            }
        }
    )

    val showEndDateDialog = remember { mutableStateOf(false) }
    val endDatePickerState = rememberDatePickerState(
        yearRange = selectableYears,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return viewModel.isSelectableEndDate(utcTimeMillis)
            }

            override fun isSelectableYear(year: Int): Boolean {
                return isSelectableYear(year)
            }
        }
    )

    Text(
        modifier = Modifier
            .padding(bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.edit_mortgage_profile_owe),
        style = runescapeTypography.bodyLarge
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(DefaultPadding)
    ) {
        DateInput(
            modifier = Modifier.padding(bottom = DefaultPadding),
            state = state.inputState.startDate,
            label = stringResource(id = R.string.start_date),
            placeholder = stringResource(id = R.string.edit_mortgage_profile_invalid_start_date),
            onClick = { showStartDateDialog.value = true }
        )

        LumbridgeDatePickerDialog(
            showDialog = showStartDateDialog,
            datePickerState = startDatePickerState,
            onSaveDate = { viewModel.onStartDateChanged(it) }
        )

        DateInput(
            modifier = Modifier.padding(bottom = DefaultPadding),
            state = state.inputState.endDate,
            label = stringResource(id = R.string.end_date),
            placeholder = stringResource(id = R.string.edit_mortgage_profile_invalid_end_date),
            onClick = { showEndDateDialog.value = true }
        )

        LumbridgeDatePickerDialog(
            showDialog = showEndDateDialog,
            datePickerState = endDatePickerState,
            onSaveDate = { viewModel.onEndDateChanged(it) }
        )

        NumberInput(
            modifier = Modifier.padding(bottom = DefaultPadding),
            state = state.inputState.loanAmount,
            label = stringResource(id = R.string.loan_amount),
            onInputChanged = { viewModel.onMortgageAmountChanged(it.toFloatOrNull()) }
        )
    }
}

@Composable
private fun ColumnScope.MortgageType(
    state: EditMortgageProfileScreenViewState.Content,
    viewModel: IEditMortgageProfileScreenViewModel
) {
    Text(
        modifier = Modifier
            .padding(bottom = HalfPadding)
            .align(Alignment.Start),
        text = stringResource(id = R.string.edit_mortgage_profile_loan),
        style = runescapeTypography.bodyLarge
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = QuarterPadding)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(DefaultPadding)
    ) {
        Text(
            modifier = Modifier.padding(bottom = HalfPadding),
            text = stringResource(id = R.string.edit_mortgage_profile_loan_type),
            style = runescapeTypography.bodyMedium
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(8.dp)
                )
                .padding(QuarterPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val backgroundColor: @Composable (isSelected: Boolean) -> Color = { isSelected ->
                if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            }

            val textColor: @Composable (isSelected: Boolean) -> Color = { isSelected ->
                if (isSelected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            }

            Text(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        viewModel.onMortgageTypeChanged(MortgageTypeUi.Variable)
                    }
                    .background(
                        color = backgroundColor(state.inputState.mortgageType?.isVariable() == true),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(QuarterPadding),
                text = stringResource(id = MortgageTypeUi.Variable.label),
                style = runescapeTypography.bodyMedium,
                textAlign = TextAlign.Center,
                color = textColor(state.inputState.mortgageType?.isVariable() == true)
            )

            Text(
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        viewModel.onMortgageTypeChanged(MortgageTypeUi.Fixed)
                    }
                    .background(
                        color = backgroundColor(state.inputState.mortgageType?.isFixed() == true),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(QuarterPadding),
                text = stringResource(id = MortgageTypeUi.Fixed.label),
                style = runescapeTypography.bodyMedium,
                textAlign = TextAlign.Center,
                color = textColor(state.inputState.mortgageType?.isFixed() == true)
            )

        }


        Spacer(modifier = Modifier.height(DefaultPadding))

        when (state.inputState.mortgageType) {
            MortgageTypeUi.Variable -> VariableMortgageInput(state, viewModel)
            MortgageTypeUi.Fixed -> FixedMortgageInput(state, viewModel)
            else -> Unit
        }
    }
}

@Composable
private fun ColumnScope.VariableMortgageInput(
    state: EditMortgageProfileScreenViewState.Content,
    viewModel: IEditMortgageProfileScreenViewModel
) {
    NumberInput(
        modifier = Modifier.padding(bottom = DefaultPadding),
        state = state.inputState.euribor,
        label = stringResource(id = R.string.euribor),
        onInputChanged = { viewModel.onEuriborRateChanged(it.toFloatOrNull()) }
    )

    NumberInput(
        modifier = Modifier.padding(bottom = DefaultPadding),
        state = state.inputState.spread,
        label = stringResource(id = R.string.spread),
        onInputChanged = { viewModel.onSpreadChanged(it.toFloatOrNull()) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
private fun ColumnScope.FixedMortgageInput(
    state: EditMortgageProfileScreenViewState.Content,
    viewModel: IEditMortgageProfileScreenViewModel
) {
    NumberInput(
        modifier = Modifier.padding(bottom = DefaultPadding),
        state = state.inputState.fixedInterestRate,
        label = stringResource(id = R.string.interest_rate),
        onInputChanged = { viewModel.onFixedInterestRateChanged(it.toFloatOrNull()) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}
