package com.example.githubapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ApiDetailUser
import com.example.domain.usecase.FetchApiUseCase
import com.example.domain.usecase.FetchPreferencesUseCase
import com.example.domain.usecase.FetchRoomUseCase
import com.example.domain.usecase.HttpExceptionUseCase
import com.example.domain.usecase.UpdateRoomUseCase
import com.example.githubapp.ui.DetailUserFragmentArgs
import kotlinx.coroutines.launch

class DetailUserViewModel(
    private val fetchRoomUseCase: FetchRoomUseCase,
    private val updateRoomUseCase: UpdateRoomUseCase,
    private val fetchPreferencesUseCase: FetchPreferencesUseCase,
    private val fetchApiUseCase: FetchApiUseCase
) : ViewModel() {
    private lateinit var _arguments: DetailUserFragmentArgs
    private var _favoriteList: MutableList<String> = mutableListOf()
    private var _userId: Int? = null

    private val _isFavorited = MutableLiveData<Boolean>()
    val isFavorited: LiveData<Boolean> = _isFavorited

    private val _detailUser = MutableLiveData<ApiDetailUser?>()
    val detailUser: LiveData<ApiDetailUser?> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun getArgs(arguments: DetailUserFragmentArgs) {
        _arguments = arguments
    }
    fun getDetailUser() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _detailUser.value = fetchApiUseCase.getDetailUser(_arguments.username)
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
    fun getFavoriteList() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _userId = fetchPreferencesUseCase.loadUserId()
                _favoriteList = fetchRoomUseCase.getFavoriteList(_userId).toMutableList()
                checkFavorite()
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e
                _isLoading.value = false
            }
        }
    }
    fun updateFavorite() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                if (_isFavorited.value == true) {
                    _favoriteList.remove(_arguments.username)
                    _isFavorited.value = false
                } else {
                    _favoriteList.add(_arguments.username)
                    _isFavorited.value = true
                }
                if (_favoriteList.isEmpty()) {
                    updateRoomUseCase.updateFavoriteList(_userId, null)
                } else {
                    updateRoomUseCase.updateFavoriteList(_userId, _favoriteList)
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e
                _isLoading.value = false
            }
        }
    }
    private fun checkFavorite() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _isFavorited.value = _favoriteList.contains(_arguments.username)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e
                _isLoading.value = false
            }
        }
    }
}