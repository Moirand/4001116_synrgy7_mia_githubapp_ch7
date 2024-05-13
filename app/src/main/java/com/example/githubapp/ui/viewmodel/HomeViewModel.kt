package com.example.githubapp.ui.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.githubapp.data.domain.SettingsPreferences
import com.example.githubapp.data.remote.response.ErrorResponse
import com.example.githubapp.data.remote.response.UserResponseItem
import com.example.githubapp.data.remote.retrofit.ApiConfig
import com.example.githubapp.di.Injection
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(private val preferences: SettingsPreferences) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _listUsers = MutableLiveData<List<UserResponseItem>?>()
    val listUsers: LiveData<List<UserResponseItem>?> = _listUsers

    private val _getMode = MutableLiveData<Boolean>()
    val getMode: LiveData<Boolean> = _getMode

    fun getMode() {
        viewModelScope.launch {
            preferences.getMode().collect {
                _getMode.value = it
            }
        }
    }

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

class HomeViewModelFactory(private val pref: SettingsPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: HomeViewModelFactory? = null
        fun getInstance(datastore: DataStore<Preferences>): HomeViewModelFactory =
            instance ?: synchronized(this) {
                instance
                    ?: HomeViewModelFactory(Injection.provideSettingsPreferences(datastore))
            }.also { instance = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(pref) as T
    }
}