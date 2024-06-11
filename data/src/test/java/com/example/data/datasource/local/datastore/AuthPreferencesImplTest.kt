package com.example.data.datasource.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import com.example.data.datasource.local.datastore.AuthPreferencesImpl.Companion.TOKEN_KEY
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class AuthPreferencesImplTest {
    // Given
    private val datastore = mock<DataStore<Preferences>>()
    private val authPreferencesImpl = AuthPreferencesImpl(datastore)
    private val token = "token"

    @Test
    fun loadToken() = runTest {
        // When
        whenever(datastore.data).thenReturn(flowOf(preferencesOf(TOKEN_KEY to token)))
        val result = authPreferencesImpl.loadToken()

        // Then
        assertEquals(token, result)
    }
}