package com.eyther.lumbridge.launcher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyther.lumbridge.domain.model.locale.SupportedLanguages
import com.eyther.lumbridge.launcher.model.MainScreenViewState
import com.eyther.lumbridge.launcher.model.UiMode
import com.eyther.lumbridge.usecase.locale.GetCurrentSystemLanguageOrDefault
import com.eyther.lumbridge.usecase.preferences.GetPreferencesStream
import com.eyther.lumbridge.usecase.preferences.SavePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getPreferencesFlow: GetPreferencesStream,
    private val savePreferences: SavePreferences,
    private val getCurrentSystemLanguageOrDefault: GetCurrentSystemLanguageOrDefault
) : ViewModel(), IMainActivityViewModel {

    override val viewState: MutableStateFlow<MainScreenViewState> =
        MutableStateFlow(MainScreenViewState())

    init {
        observePreferences()
    }

    private fun observePreferences() {
        getPreferencesFlow()
            .filterNotNull()
            .onEach { preferences ->
                viewState.update { oldState ->
                    oldState.copy(
                        uiMode = if (preferences.isDarkMode) UiMode.Dark else UiMode.Light,
                        appLanguageCountryCode = preferences.appLanguage.countryCode
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    override suspend fun hasStoredPreferences(): Boolean {
        return getPreferencesFlow().firstOrNull() != null
    }

    override suspend fun updateSettings(
        isDarkMode: Boolean?,
        appLanguageCountryCode: String?,
        showAllocationsOnExpenses: Boolean?,
        addFoodCardToNecessitiesAllocation: Boolean?
    ) {
        val newDarkMode = (isDarkMode ?: getPreferencesFlow().firstOrNull()?.isDarkMode) == true
        val newAppLanguage = SupportedLanguages.getOrNull(appLanguageCountryCode) ?: getCurrentSystemLanguageOrDefault()
        val newShowAllocationsOnExpenses = (showAllocationsOnExpenses ?: getPreferencesFlow().firstOrNull()?.showAllocationsOnExpenses) != false
        val newAddFoodCardToNecessitiesAllocation = (addFoodCardToNecessitiesAllocation ?: getPreferencesFlow().firstOrNull()?.addFoodCardToNecessitiesAllocation) != false

        savePreferences(
            isDarkMode = newDarkMode,
            appLanguages = newAppLanguage,
            showAllocationsOnExpenses = newShowAllocationsOnExpenses,
            addFoodCardToNecessitiesAllocation = newAddFoodCardToNecessitiesAllocation
        )
    }
}
