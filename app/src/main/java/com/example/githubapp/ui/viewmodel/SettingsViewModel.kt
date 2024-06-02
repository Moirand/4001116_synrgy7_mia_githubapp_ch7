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

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getMode() {
        viewModelScope.launch {
            try {
                _getMode.value = fetchPreferencesUseCase.loadMode()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun setMode(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            try {
                updatePreferencesUseCase.saveMode(isDarkModeActive)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
