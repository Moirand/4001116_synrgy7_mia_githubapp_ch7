package com.example.githubapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapp.data.remote.response.DetailUserResponse
import com.example.githubapp.data.remote.response.ErrorResponse
import com.example.githubapp.data.remote.retrofit.ApiConfig
import com.example.githubapp.ui.DetailUserFragmentArgs
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailUserViewModel : ViewModel() {
    private lateinit var _arguments: DetailUserFragmentArgs

    private val _detailUser = MutableLiveData<DetailUserResponse?>()
    val detailUser: LiveData<DetailUserResponse?> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getArgs(arguments: DetailUserFragmentArgs) {
        _arguments = arguments
    }

    fun getDetailUser(context: Context) {
        try {
            viewModelScope.launch {
                _isLoading.value = true
                _detailUser.value =
                    ApiConfig.provideApiService(context).getDetailUser(_arguments.username)
                _isLoading.value = false
            }
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