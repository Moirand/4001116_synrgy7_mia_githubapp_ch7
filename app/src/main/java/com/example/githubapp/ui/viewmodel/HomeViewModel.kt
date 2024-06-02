package com.example.githubapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ApiUser
import com.example.domain.usecase.FetchApiUseCase
import com.example.domain.usecase.FetchPreferencesUseCase
import com.example.domain.usecase.HttpExceptionUseCase
import com.example.domain.usecase.UpdatePreferencesUseCase
import com.example.domain.usecase.UpdateRoomUseCase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel(
    private val fetchPreferencesUseCase: FetchPreferencesUseCase,
    private val fetchApiUseCase: FetchApiUseCase,
    private val updatePreferencesUseCase: UpdatePreferencesUseCase,
    private val updateRoomUseCase: UpdateRoomUseCase,
) : ViewModel() {
    private val _listUsers = MutableLiveData<List<ApiUser>?>()
    val listUsers: LiveData<List<ApiUser>?> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun getUsers(username: String? = null) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _listUsers.value = if (username.isNullOrEmpty()) {
                    fetchApiUseCase.getAllUsers()
                } else {
                    fetchApiUseCase.getSearchedUsername(username).items
                }
                _isLoading.value = false
            } catch (e: HttpExceptionUseCase) {
                _error.value = e
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e
                _isLoading.value = false
            }
        }
    }
    fun deleteAccount() {
        viewModelScope.launch {
            try {
                fetchPreferencesUseCase.loadUserId()?.let { updateRoomUseCase.deleteUser(it) }
            } catch (e: Exception) {
                _error.value = e
            }
        }
    }
    fun signOut(): Deferred<Unit> =
        viewModelScope.async {
            try {
                updatePreferencesUseCase.deleteToken()
                updatePreferencesUseCase.deleteUserId()
            } catch (e: Exception) {
                _error.value = e
            }
        }
}