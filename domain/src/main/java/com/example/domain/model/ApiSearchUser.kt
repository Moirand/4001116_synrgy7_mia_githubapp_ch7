package com.example.domain.model

data class ApiSearchUser(
    val totalCount: Int? = null,
    val incompleteResults: Boolean? = null,
    val items: List<ApiUser>? = null
)