package com.example.githubapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.FetchPreferencesUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val fetchPreferencesUseCase: FetchPreferencesUseCase
) : ViewModel() {
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
                _getMode.value = fetchPreferencesUseCase.loadMode()
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
                _isLoggedIn.value = !fetchPreferencesUseCase.loadToken().isNullOrEmpty()
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e
                _isLoading.value = false
            }
        }
    }
}