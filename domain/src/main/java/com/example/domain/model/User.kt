package com.example.domain.model

data class User(
    val userId: Int,
    val username: String,
    val email: String,
    val password: String,
    val favoriteList: String? = null,
)