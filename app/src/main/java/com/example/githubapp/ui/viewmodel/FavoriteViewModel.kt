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
import com.example.githubapp.data.remote.response.ErrorResponse
import com.example.githubapp.data.remote.response.UserResponseItem
import com.example.githubapp.data.remote.retrofit.ApiConfig
import com.example.githubapp.di.Injection
import com.example.githubapp.ui.utill.toUserResponseItem
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FavoriteViewModel(
    private val userRepository: UserRepository,
    private val preferences: SettingsPreferences
) : ViewModel() {
    private var _userId = -1

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception> = _error

    private val _listUsers: MutableList<UserResponseItem> = mutableListOf()
    val listUsers: LiveData<List<UserResponseItem>> = MutableLiveData(_listUsers)

    fun getUsers(context: Context) {
        viewModelScope.launch {
            try {
                preferences.loadUserId().collect { id ->
                    _isLoading.value = true
                    _userId = id
                    _listUsers.clear()
                    val listFavorite = userRepository.getFavoriteList(_userId)
                    listFavorite.forEach {
                        _listUsers.add(
                            ApiConfig.provideApiService(context).getDetailUser(it)
                                .toUserResponseItem()
                        )
                    }
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
    }
}

class FavoriteViewModelFactory(
    private val userRepository: UserRepository,
    private val preferences: SettingsPreferences
) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: FavoriteViewModelFactory? = null
        fun getInstance(
            context: Context,
            datastore: DataStore<Preferences>
        ): FavoriteViewModelFactory =
            instance ?: synchronized(this) {
                instance
                    ?: FavoriteViewModelFactory(
                        Injection.provideUserRepository(context),
                        Injection.provideSettingsPreferences(datastore)
                    )
            }.also { instance = it }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteViewModel(userRepository, preferences) as T
    }
}