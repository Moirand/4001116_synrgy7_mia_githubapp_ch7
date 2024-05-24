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
import com.example.githubapp.data.domain.UserRepository
import com.example.githubapp.data.remote.response.DetailUserResponse
import com.example.githubapp.data.remote.response.ErrorResponse
import com.example.githubapp.data.remote.retrofit.ApiConfig
import com.example.githubapp.di.Injection
import com.example.githubapp.ui.DetailUserFragmentArgs
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailUserViewModel(
    private val userRepository: UserRepository,
    private val preferences: SettingsPreferences
) : ViewModel() {
    private lateinit var _arguments: DetailUserFragmentArgs
    private var _favoriteList: MutableList<String> = mutableListOf()
    private var _userId = -1

    private val _isFavorited = MutableLiveData<Boolean>()
    val isFavorited: LiveData<Boolean> = _isFavorited

    private val _detailUser = MutableLiveData<DetailUserResponse?>()
    val detailUser: LiveData<DetailUserResponse?> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

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
                _error.value = Exception(error.message)
            } else {
                _error.value = e
            }
            _isLoading.value = false
        }
    }

    fun getFavoriteList() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                preferences.loadUserId().collect { id ->
                    _userId = id
                    _favoriteList = userRepository.getFavoriteList(_userId).toMutableList()
                    checkFavorite()
                }
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
                    userRepository.updateFavoriteList(_userId, null)
                } else {
                    userRepository.updateFavoriteList(_userId, _favoriteList)
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

class DetailUserViewModelFactory(
    private val userRepository: UserRepository,
    private val preferences: SettingsPreferences
) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: DetailUserViewModelFactory? = null
        fun getInstance(
            context: Context,
            datastore: DataStore<Preferences>
        ): DetailUserViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: DetailUserViewModelFactory(
                    Injection.provideUserRepository(context),
                    Injection.provideSettingsPreferences(datastore)
                )
            }.also { instance = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailUserViewModel(userRepository, preferences) as T
    }
}