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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(private val preferences: SettingsPreferences) : ViewModel() {
    private val _getMode = MutableLiveData<Boolean>()
    val getMode: LiveData<Boolean> = _getMode

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getMode() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                preferences.loadMode().collect {
                    _getMode.value = it
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e
                _isLoading.value = false
            }
        }
    }

    fun checkLogIn() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                delay(3000L)
                preferences.loadToken().collect {
                    _isLoggedIn.value = !it.isNullOrEmpty()
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e
                _isLoading.value = false
            }
        }
    }
}

class SplashViewModelFactory(private val preferences: SettingsPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: SplashViewModelFactory? = null
        fun getInstance(datastore: DataStore<Preferences>): SplashViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: SplashViewModelFactory(Injection.provideSettingsPreferences(datastore))
            }.also { instance = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SplashViewModel(preferences) as T
    }
}