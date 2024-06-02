package com.example.domain.usecase

class HttpExceptionUseCase(message: String?, cause: Throwable? = null) : Exception(message, cause)