package com.example.githubapp.ui.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.githubapp.data.domain.AuthRepository
import com.example.githubapp.data.domain.SettingsPreferences
import com.example.githubapp.data.domain.UserRepository
import com.example.githubapp.di.Injection
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val preferences: SettingsPreferences
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
                _isSuccess.value = authRepository.isEmailPasswordExist(email, password)
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
                preferences.saveToken(token)
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
                val userId = userRepository.getUserIdByEmail(email)
                preferences.saveUserId(userId)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e
                _isLoading.value = false
                _isSuccess.value = false
            }
        }
    }
}

class LoginViewModelFactory(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val preferences: SettingsPreferences
) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: LoginViewModelFactory? = null
        fun getInstance(
            context: Context,
            datastore: DataStore<Preferences>
        ): LoginViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: LoginViewModelFactory(
                    Injection.provideAuthRepository(context),
                    Injection.provideUserRepository(context),
                    Injection.provideSettingsPreferences(datastore)
                )
            }.also { instance = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(authRepository, userRepository, preferences) as T
    }
}