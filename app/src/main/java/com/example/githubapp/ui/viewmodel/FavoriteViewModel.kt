package com.example.githubapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ApiUser
import com.example.domain.usecase.FetchApiUseCase
import com.example.domain.usecase.FetchPreferencesUseCase
import com.example.domain.usecase.FetchRoomUseCase
import com.example.domain.usecase.HttpExceptionUseCase
import com.example.githubapp.ui.toApiUser
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val fetchRoomUseCase: FetchRoomUseCase,
    private val fetchPreferencesUseCase: FetchPreferencesUseCase,
    private val fetchApiUseCase: FetchApiUseCase
) : ViewModel() {
    private var _userId: Int? = null

    private val _listUsers: MutableList<ApiUser> = mutableListOf()
    val listUsers: LiveData<List<ApiUser>> = MutableLiveData(_listUsers)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun getUsers() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _userId = fetchPreferencesUseCase.loadUserId()
                _listUsers.clear()
                val listFavorite = fetchRoomUseCase.getFavoriteList(_userId)
                listFavorite.forEach {
                    _listUsers.add(fetchApiUseCase.getDetailUser(it).toApiUser())
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
}