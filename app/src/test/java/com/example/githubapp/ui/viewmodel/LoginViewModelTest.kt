package com.example.githubapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.domain.model.User
import com.example.domain.usecase.FetchRoomUseCase
import com.example.domain.usecase.UpdatePreferencesUseCase
import com.example.githubapp.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.mock
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class LoginViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Given
    private val fetchRoomUseCase = mock<FetchRoomUseCase>()
    private val updatePreferencesUseCase = mock<UpdatePreferencesUseCase>()
    private val user = mock<User>()

    private val viewmodel = LoginViewModel(
        fetchRoomUseCase = fetchRoomUseCase,
        updatePreferencesUseCase = updatePreferencesUseCase
    )
    private val isSuccessObserver = mock<Observer<Boolean>>()
    private val idLoadingObserver = mock<Observer<Boolean>>()
    private val errorObserver = mock<Observer<Exception>>()

    private val isSuccessCaptor = argumentCaptor<Boolean>()
    private val isLoadingCaptor = argumentCaptor<Boolean>()
    private val errorCaptor = argumentCaptor<Exception>()

    @Test
    fun `login() successful`() = runTest {
        // Given
        val email = "email"
        val password = "password"
        viewmodel.isSuccess.observeForever(isSuccessObserver)
        viewmodel.isLoading.observeForever(idLoadingObserver)

        // When
        whenever(fetchRoomUseCase.getUserByEmailPassword(email, password)).thenReturn(user)
        viewmodel.login(email, password)

        // Verify
        verify(idLoadingObserver, times(2)).onChanged(isLoadingCaptor.capture())
        verify(isSuccessObserver).onChanged(isSuccessCaptor.capture())
        assertEquals(isLoadingCaptor.allValues, listOf(true, false))
        assertEquals(isSuccessCaptor.allValues, listOf(true))
    }

    @Test
    fun `login() failed`() = runTest {
        // Given
        val email = "email"
        val password = "password"
        viewmodel.isSuccess.observeForever(isSuccessObserver)
        viewmodel.isLoading.observeForever(idLoadingObserver)

        // When
        whenever(fetchRoomUseCase.getUserByEmailPassword(email, password)).thenReturn(null)
        viewmodel.login(email, password)

        // Verify
        verify(idLoadingObserver, times(2)).onChanged(isLoadingCaptor.capture())
        verify(isSuccessObserver).onChanged(isSuccessCaptor.capture())
        assertEquals(isLoadingCaptor.allValues, listOf(true, false))
        assertEquals(isSuccessCaptor.allValues, listOf(false))
    }

    @Test
    fun `login() error handling`() = runTest {
        // Given
        val email = "email"
        val password = "password"
        val throwable = UnsupportedOperationException()
        viewmodel.isLoading.observeForever(idLoadingObserver)
        viewmodel.error.observeForever(errorObserver)

        // When
        whenever(fetchRoomUseCase.getUserByEmailPassword(email, password)).thenThrow(throwable)
        viewmodel.login(email, password)

        // Verify
        verify(idLoadingObserver, times(2)).onChanged(isLoadingCaptor.capture())
        verify(errorObserver).onChanged(errorCaptor.capture())
        assertEquals(isLoadingCaptor.allValues, listOf(true, false))
        assertEquals(errorCaptor.allValues, listOf(throwable))
    }

    @Test
    fun `saveToken() successful`() = runTest {
        // Given
        val token = "token"
        viewmodel.isSuccess.observeForever(isSuccessObserver)
        viewmodel.isLoading.observeForever(idLoadingObserver)

        // When
        viewmodel.saveToken(token)

        // Verify
        verify(idLoadingObserver, times(2)).onChanged(isLoadingCaptor.capture())
        verify(isSuccessObserver).onChanged(isSuccessCaptor.capture())
        assertEquals(isLoadingCaptor.allValues, listOf(true, false))
        assertEquals(isSuccessCaptor.allValues, listOf(true))
    }

    @Test
    fun `saveToken() error handling`() = runTest {
        // Given
        val token = "token"
        val throwable = UnsupportedOperationException()
        viewmodel.isLoading.observeForever(idLoadingObserver)
        viewmodel.error.observeForever(errorObserver)

        // When
        whenever(updatePreferencesUseCase.saveToken(token)).thenThrow(throwable)
        viewmodel.saveToken(token)

        // Verify
        verify(idLoadingObserver, times(2)).onChanged(isLoadingCaptor.capture())
        verify(errorObserver).onChanged(errorCaptor.capture())
        assertEquals(isLoadingCaptor.allValues, listOf(true, false))
        assertEquals(errorCaptor.allValues, listOf(throwable))
    }

    @Test
    fun `saveUserId() successful`() = runTest {
        // Given
        val email = "email"
        val userId = 0
        viewmodel.isSuccess.observeForever(isSuccessObserver)
        viewmodel.isLoading.observeForever(idLoadingObserver)

        // When
        whenever(fetchRoomUseCase.getUserIdByEmail(email)).thenReturn(userId)
        viewmodel.saveUserId(email)

        // Verify
        verify(idLoadingObserver, times(2)).onChanged(isLoadingCaptor.capture())
        verify(isSuccessObserver).onChanged(isSuccessCaptor.capture())
        assertEquals(isLoadingCaptor.allValues, listOf(true, false))
        assertEquals(isSuccessCaptor.allValues, listOf(true))
    }

    @Test
    fun `saveUserId() error handling`() = runTest {
        // Given
        val email = "email"
        val userId = 0
        val throwable = UnsupportedOperationException()
        viewmodel.isLoading.observeForever(idLoadingObserver)
        viewmodel.error.observeForever(errorObserver)

        // When
        whenever(fetchRoomUseCase.getUserIdByEmail(email)).thenReturn(userId)
        whenever(updatePreferencesUseCase.saveUserId(userId)).thenThrow(throwable)
        viewmodel.saveUserId(email)

        // Verify
        verify(idLoadingObserver, times(2)).onChanged(isLoadingCaptor.capture())
        verify(errorObserver).onChanged(errorCaptor.capture())
        assertEquals(isLoadingCaptor.allValues, listOf(true, false))
        assertEquals(errorCaptor.allValues, listOf(throwable))
    }
}