package com.example.githubapp.ui.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.githubapp.data.domain.SettingsPreferences
import com.example.githubapp.di.Injection
import kotlinx.coroutines.launch

class SettingsViewModel(private val preferences: SettingsPreferences) : ViewModel() {
    private val _getMode = MutableLiveData<Boolean>()
    val getMode: LiveData<Boolean> = _getMode

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getMode() {
        viewModelScope.launch {
            try {
                preferences.loadMode().collect {
                    _getMode.value = it
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun setMode(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            try {
                preferences.saveMode(isDarkModeActive)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}

class SettingsViewModelFactory(private val pref: SettingsPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: SettingsViewModelFactory? = null
        fun getInstance(datastore: DataStore<Preferences>): SettingsViewModelFactory =
            instance ?: synchronized(this) {
                instance
                    ?: SettingsViewModelFactory(Injection.provideSettingsPreferences(datastore))
            }.also { instance = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(pref) as T
    }
}
