package com.example.data.datasource.remote.retrofit

import com.example.data.datasource.remote.response.DetailUserResponse
import com.example.data.datasource.remote.response.SearchResponse
import com.example.data.datasource.remote.response.UserResponseItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getAllUsers(): List<UserResponseItem>

    @GET("search/users")
    suspend fun getSearchedUsername(@Query("q") username: String): SearchResponse

    @GET("users/{username}")
    suspend fun getDetailUser(@Path("username") username: String): DetailUserResponse

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): List<UserResponseItem>

    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): List<UserResponseItem>
}