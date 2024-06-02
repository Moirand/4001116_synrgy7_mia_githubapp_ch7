package com.example.githubapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.FetchRoomUseCase
import com.example.domain.usecase.UpdatePreferencesUseCase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginViewModel(
    private val fetchRoomUseCase: FetchRoomUseCase,
    private val updatePreferencesUseCase: UpdatePreferencesUseCase
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _isSuccess.value = fetchRoomUseCase.getUserByEmailPassword(email, password) != null
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e
                _isLoading.value = false
                _isSuccess.value = false
            }
        }
    }

    fun saveToken(token: String): Deferred<Unit> {
        return viewModelScope.async {
            try {
                _isLoading.value = true
                updatePreferencesUseCase.saveToken(token)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e
                _isLoading.value = false
                _isSuccess.value = false
            }
        }
    }


    fun saveUserId(email: String): Deferred<Unit> {
        return viewModelScope.async {
            try {
                _isLoading.value = true
                val userId = fetchRoomUseCase.getUserIdByEmail(email)
                updatePreferencesUseCase.saveUserId(userId)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e
                _isLoading.value = false
                _isSuccess.value = false
            }
        }
    }
}