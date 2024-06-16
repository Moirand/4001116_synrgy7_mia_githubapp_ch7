package com.example.data.repository

import com.example.data.datasource.local.room.UserDao
import com.example.domain.model.User
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class UserRepositoryImplTest {
    // Given
    private val userDao = mock<UserDao>()
    private val userRepositoryImpl = UserRepositoryImpl(userDao)
    private val email = "email"
    private val password = "password"
    private val user = User(
        userId = 1,
        username = "username",
        email = email,
        password = password
    )
    @Test
    fun getUserIdByEmail() = runTest {
        // When
        whenever(userDao.getUserIdByEmail(email)).thenReturn(user.userId)
        val expected = user.userId
        val actual = userRepositoryImpl.getUserIdByEmail(email)

        // Then
        assertEquals(expected, actual)
    }
}