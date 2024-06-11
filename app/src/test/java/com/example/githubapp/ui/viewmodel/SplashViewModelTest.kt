package com.example.githubapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.domain.usecase.FetchPreferencesUseCase
import com.example.githubapp.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SplashViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Given
    private val fetchPreferencesUseCase = mock<FetchPreferencesUseCase>()
    private val viewmodel = SplashViewModel(
        fetchPreferencesUseCase = fetchPreferencesUseCase,
    )
    private val modeObserver = mock<Observer<Boolean>>()
    private val isLoggedInObserver = mock<Observer<Boolean>>()
    private val idLoadingObserver = mock<Observer<Boolean>>()
    private val errorObserver = mock<Observer<Exception>>()

    private val modeCaptor = argumentCaptor<Boolean>()
    private val isLoadingCaptor = argumentCaptor<Boolean>()
    private val isLoggedInCaptor = argumentCaptor<Boolean>()
    private val errorCaptor = argumentCaptor<Exception>()

    @Test
    fun isDarkModeActivated() = runTest {
        // Given
        viewmodel.getMode.observeForever(modeObserver)
        viewmodel.isLoading.observeForever(idLoadingObserver)

        // When
        whenever(fetchPreferencesUseCase.loadMode()).thenReturn(true)
        viewmodel.getMode()

        // Verify
        verify(idLoadingObserver, times(2)).onChanged(isLoadingCaptor.capture())
        verify(modeObserver).onChanged(modeCaptor.capture())
        assertEquals(isLoadingCaptor.allValues, listOf(true, false))
        assertEquals(modeCaptor.allValues, listOf(true))
    }

    @Test
    fun isDarkModeDeactivated() = runTest {
        // Given
        viewmodel.getMode.observeForever(modeObserver)
        viewmodel.isLoading.observeForever(idLoadingObserver)

        // When
        whenever(fetchPreferencesUseCase.loadMode()).thenReturn(false)
        viewmodel.getMode()

        // Verify
        verify(idLoadingObserver, times(2)).onChanged(isLoadingCaptor.capture())
        verify(modeObserver).onChanged(modeCaptor.capture())
        assertEquals(isLoadingCaptor.allValues, listOf(true, false))
        assertEquals(modeCaptor.allValues, listOf(false))
    }

    @Test
    fun `getMode() error handling`() = runTest {
        // Given
        val throwable = UnsupportedOperationException()
        viewmodel.isLoading.observeForever(idLoadingObserver)
        viewmodel.error.observeForever(errorObserver)

        // When
        whenever(fetchPreferencesUseCase.loadMode()).thenThrow(throwable)
        viewmodel.getMode()

        // Verify
        verify(idLoadingObserver, times(2)).onChanged(isLoadingCaptor.capture())
        verify(errorObserver).onChanged(errorCaptor.capture())
        assertEquals(isLoadingCaptor.allValues, listOf(true, false))
        assertEquals(errorCaptor.allValues, listOf(throwable))
    }

    @Test
    fun isAlreadyLoggedIn() = runTest {
        // Given
        val token = "token"
        viewmodel.isLoggedIn.observeForever(isLoggedInObserver)
        viewmodel.isLoading.observeForever(idLoadingObserver)

        // When
        whenever(fetchPreferencesUseCase.loadToken()).thenReturn(token)
        viewmodel.checkLogIn()

        delay(3000L)

        // Verify
        verify(idLoadingObserver, times(2)).onChanged(isLoadingCaptor.capture())
        verify(isLoggedInObserver).onChanged(isLoggedInCaptor.capture())
        assertEquals(isLoadingCaptor.allValues, listOf(true, false))
        assertEquals(isLoggedInCaptor.allValues, listOf(true))
    }

    @Test
    fun isNotLoggedInYet() = runTest {
        // Given
        viewmodel.isLoggedIn.observeForever(isLoggedInObserver)
        viewmodel.isLoading.observeForever(idLoadingObserver)

        // When
        whenever(fetchPreferencesUseCase.loadToken()).thenReturn(null)
        viewmodel.checkLogIn()

        delay(3000L)

        // Verify
        verify(idLoadingObserver, times(2)).onChanged(isLoadingCaptor.capture())
        verify(isLoggedInObserver).onChanged(isLoggedInCaptor.capture())
        assertEquals(isLoadingCaptor.allValues, listOf(true, false))
        assertEquals(isLoggedInCaptor.allValues, listOf(false))
    }

    @Test
    fun `checkLogIn() error handling`() = runTest {
        // Given
        val throwable = UnsupportedOperationException()
        viewmodel.isLoading.observeForever(idLoadingObserver)
        viewmodel.error.observeForever(errorObserver)

        // When
        whenever(fetchPreferencesUseCase.loadToken()).thenThrow(throwable)
        viewmodel.checkLogIn()

        delay(3000L)

        // Verify
        verify(idLoadingObserver, times(2)).onChanged(isLoadingCaptor.capture())
        verify(errorObserver).onChanged(errorCaptor.capture())
        assertEquals(isLoadingCaptor.allValues, listOf(true, false))
        assertEquals(errorCaptor.allValues, listOf(throwable))
    }
}