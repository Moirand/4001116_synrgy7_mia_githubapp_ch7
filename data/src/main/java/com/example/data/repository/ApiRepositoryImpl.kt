package com.example.data.repository

import com.example.data.datasource.remote.response.ErrorResponse
import com.example.data.datasource.remote.retrofit.ApiService
import com.example.data.toApiDetailUser
import com.example.data.toApiSearchUser
import com.example.data.toApiUser
import com.example.domain.model.ApiDetailUser
import com.example.domain.model.ApiSearchUser
import com.example.domain.model.ApiUser
import com.example.domain.repository.ApiRepository
import com.example.domain.usecase.HttpExceptionUseCase
import com.google.gson.Gson
import retrofit2.HttpException

class ApiRepositoryImpl(private val apiService: ApiService) : ApiRepository {
    override suspend fun getAllUsers(): List<ApiUser> =
        try {
            apiService.getAllUsers().map { it.toApiUser() }
        } catch (e: HttpException) {
            val json = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(json, ErrorResponse::class.java)
            throw HttpExceptionUseCase(error.message, e)
        }

    override suspend fun getSearchedUsername(username: String): ApiSearchUser =
        try {
            apiService.getSearchedUsername(username).toApiSearchUser()
        } catch (e: HttpException) {
            val json = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(json, ErrorResponse::class.java)
            throw HttpExceptionUseCase(error.message, e)
        }

    override suspend fun getDetailUser(username: String): ApiDetailUser =
        try {
            apiService.getDetailUser(username).toApiDetailUser()
        } catch (e: HttpException) {
            val json = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(json, ErrorResponse::class.java)
            throw HttpExceptionUseCase(error.message, e)
        }

    override suspend fun getFollowers(username: String): List<ApiUser> =
        try {
            apiService.getFollowers(username).map { it.toApiUser() }
        } catch (e: HttpException) {
            val json = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(json, ErrorResponse::class.java)
            throw HttpExceptionUseCase(error.message, e)
        }

    override suspend fun getFollowing(username: String): List<ApiUser> =
        try {
            apiService.getFollowing(username).map { it.toApiUser() }
        } catch (e: HttpException) {
            val json = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(json, ErrorResponse::class.java)
            throw HttpExceptionUseCase(error.message, e)
        }
}