package com.example.data.repository

import com.example.data.datasource.local.room.UserDao
import com.example.data.datasource.local.room.UserEntity
import com.example.data.datasource.remote.retrofit.ApiService
import com.example.data.toUser
import com.example.data.toUserEntity
import com.example.domain.model.User
import com.example.domain.usecase.FetchRoomUseCase
import com.example.domain.usecase.UpdatePreferencesUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.whenever

class AuthRepositoryImplTest {
    // Given
    private val userDao = mock<UserDao>()
    private val authRepositoryImpl = AuthRepositoryImpl(userDao)
    private val email = "email"
    private val password = "password"
    private val user = User(
        userId = 1,
        username = "username",
        email = email,
        password = password
    )
    private val userEntity = UserEntity(
        userId = 1,
        username = "username",
        email = email,
        password = password
    )

    @Test
    fun getUserByEmailPassword() = runTest {
        // When
        whenever(userDao.getUserByUsernamePassword(email, password)).thenReturn(userEntity)
        val expected = user
        val actual = authRepositoryImpl.getUserByEmailPassword(email, password)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun getUserByEmail() = runTest {
        // When
        whenever(userDao.getUserByEmail(email)).thenReturn(userEntity)
        val expected = user
        val actual = authRepositoryImpl.getUserByEmail(email)

        // Then
        assertEquals(expected, actual)
    }
}