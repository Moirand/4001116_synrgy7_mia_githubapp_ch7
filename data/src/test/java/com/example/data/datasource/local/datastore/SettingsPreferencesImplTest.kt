package com.example.data.datasource.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.preferencesOf
import com.example.data.datasource.local.datastore.SettingsPreferencesImpl.Companion.MODE_KEY
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class SettingsPreferencesImplTest {
    // Given
    private val datastore = mock<DataStore<Preferences>>()
    private val settingsPreferencesImpl = SettingsPreferencesImpl(datastore)
    private val mode = false

    @Test
    fun loadMode() = runTest {
        // When
        whenever(datastore.data).thenReturn(flowOf(preferencesOf(MODE_KEY to mode)))
        val result = settingsPreferencesImpl.loadMode()

        // Then
        Assert.assertEquals(mode, result)
    }
}