package com.example.githubapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapp.data.remote.response.ErrorResponse
import com.example.githubapp.data.remote.response.UserResponseItem
import com.example.githubapp.data.remote.retrofit.ApiConfig
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _listUsers = MutableLiveData<List<UserResponseItem>?>()
    val listUsers: LiveData<List<UserResponseItem>?> = _listUsers

    fun getAllUsers(context: Context, username: String? = null) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _listUsers.value = if (username.isNullOrEmpty()) {
                    ApiConfig.provideApiService(context).getAllUsers()
                } else {
                    ApiConfig.provideApiService(context).getSearchedUsername(username).items
                }
                _isLoading.value = false
            } catch (e: Exception) {
                if (e is HttpException) {
                    val json = e.response()?.errorBody()?.string()
                    val error = Gson().fromJson(json, ErrorResponse::class.java)
                    _error.value = error.message
                } else {
                    _error.value = e.message
                }
            }
        }
    }
}