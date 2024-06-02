package com.example.githubapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.FetchPreferencesUseCase
import com.example.domain.usecase.UpdatePreferencesUseCase
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val fetchPreferencesUseCase: FetchPreferencesUseCase,
    private val updatePreferencesUseCase: UpdatePreferencesUseCase
) : ViewModel() {
    private val _getMode = MutableLiveData<Boolean>()
    val getMode: LiveData<Boolean> = _getMode

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun getMode() {
        viewModelScope.launch {
            try {
                _getMode.value = fetchPreferencesUseCase.loadMode()
            } catch (e: Exception) {
                _error.value = e
            }
        }
    }
    fun setMode(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            try {
                updatePreferencesUseCase.saveMode(isDarkModeActive)
                _getMode.value = isDarkModeActive
            } catch (e: Exception) {
                _error.value = e
            }
        }
    }
}