package com.example.githubapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.githubapp.data.domain.AuthRepository
import com.example.githubapp.data.local.room.UserEntity
import com.example.githubapp.data.remote.response.ErrorResponse
import com.example.githubapp.di.Injection
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private var _isEmailExist = MutableLiveData<Boolean>()
    val isEmailExist: LiveData<Boolean> = _isEmailExist

    private var _isUsernameExist = MutableLiveData<Boolean>()
    val isUsernameExist: LiveData<Boolean> = _isUsernameExist

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun checkEmail(email: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _isEmailExist.value = authRepository.isEmailExist(email)
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
                _isUsernameExist.value = authRepository.isUsernameExist(username)
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
                authRepository.insertUser(
                    UserEntity(
                        username = username,
                        email = email,
                        password = password
                    )
                )
                _isLoading.value = false
                _isSuccess.value = true
            } catch (e: Exception) {
                if (e is HttpException) {
                    val json = e.response()?.errorBody()?.string()
                    val error = Gson().fromJson(json, ErrorResponse::class.java)
                    _error.value = Exception(error.message)
                } else {
                    _error.value = e
                }
            }
        }
    }
}

class RegisterViewModelFactory(private val userRepository: AuthRepository) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: RegisterViewModelFactory? = null
        fun getInstance(context: Context): RegisterViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: RegisterViewModelFactory(Injection.provideAuthRepository(context))
            }.also { instance = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterViewModel(userRepository) as T
    }
}