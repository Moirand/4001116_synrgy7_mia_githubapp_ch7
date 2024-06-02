package com.example.githubapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.User
import com.example.domain.usecase.FetchRoomUseCase
import com.example.domain.usecase.HttpExceptionUseCase
import com.example.domain.usecase.UpdateRoomUseCase
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val fetchRoomUseCase: FetchRoomUseCase,
    private val updateRoomUseCase: UpdateRoomUseCase
) : ViewModel() {
    private var _isEmailExist = MutableLiveData<Boolean>()
    val isEmailExist: LiveData<Boolean> = _isEmailExist

    private var _isUsernameExist = MutableLiveData<Boolean>()
    val isUsernameExist: LiveData<Boolean> = _isUsernameExist

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun checkEmail(email: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _isEmailExist.value = fetchRoomUseCase.getUserByEmail(email) != null
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e
            }
        }
    }
    fun checkUsername(username: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _isUsernameExist.value = fetchRoomUseCase.getUserByUsername(username) != null
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e
            }
        }
    }
    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                updateRoomUseCase.insertUser(
                    User(
                        userId = 0,
                        username = username,
                        email = email,
                        password = password
                    )
                )
                _isLoading.value = false
                _isSuccess.value = true
            } catch (e: HttpExceptionUseCase) {
                _error.value = e
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e
                _isLoading.value = false
            }
        }
    }
}