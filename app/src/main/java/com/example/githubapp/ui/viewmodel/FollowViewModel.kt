package com.example.githubapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ApiUser
import com.example.domain.usecase.FetchApiUseCase
import com.example.domain.usecase.HttpExceptionUseCase
import kotlinx.coroutines.launch

class FollowViewModel(
    private val fetchApiUseCase: FetchApiUseCase
) : ViewModel() {
    private lateinit var _username: String

    private val _listFollowers = MutableLiveData<List<ApiUser>>()
    val listFollowers: LiveData<List<ApiUser>> = _listFollowers

    private val _listFollowing = MutableLiveData<List<ApiUser>>()
    val listFollowing: LiveData<List<ApiUser>> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun getUsername(username: String?) {
        _username = username ?: ""
    }
    fun getFollowers() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _listFollowers.value = fetchApiUseCase.getFollowers(_username)
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
    fun getFollowing() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _listFollowing.value = fetchApiUseCase.getFollowing(_username)
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
}