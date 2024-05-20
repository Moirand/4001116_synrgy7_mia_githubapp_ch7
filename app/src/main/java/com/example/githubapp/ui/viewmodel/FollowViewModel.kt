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

class FollowViewModel : ViewModel() {
    private lateinit var _username: String

    private val _listFollowers = MutableLiveData<List<UserResponseItem>>()
    val listFollowers: LiveData<List<UserResponseItem>> = _listFollowers

    private val _listFollowing = MutableLiveData<List<UserResponseItem>>()
    val listFollowing: LiveData<List<UserResponseItem>> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    fun getUsername(username: String?) {
        _username = username ?: ""
    }

    fun getFollowers(context: Context) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _listFollowers.value = ApiConfig.provideApiService(context).getFollowers(_username)
                _isLoading.value = false
            } catch (e: Exception) {
                if (e is HttpException) {
                    val json = e.response()?.errorBody()?.string()
                    val error = Gson().fromJson(json, ErrorResponse::class.java)
                    _error.value = Exception(error.message)
                } else {
                    _error.value = e
                }
                _isLoading.value = false
            }
        }
    }

    fun getFollowing(context: Context) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _listFollowing.value = ApiConfig.provideApiService(context).getFollowing(_username)
                _isLoading.value = false
            } catch (e: Exception) {
                if (e is HttpException) {
                    val json = e.response()?.errorBody()?.string()
                    val error = Gson().fromJson(json, ErrorResponse::class.java)
                    _error.value = Exception(error.message)
                } else {
                    _error.value = e
                }
                _isLoading.value = false
            }
        }
    }
}