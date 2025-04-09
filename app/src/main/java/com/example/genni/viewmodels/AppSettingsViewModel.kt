package com.example.genni.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AppSettingsViewModel : ViewModel() {
    var isDarkTheme by mutableStateOf(false)
        private set

    var notificationsEnabled by mutableStateOf(true)
        private set

    var prefersMetric by mutableStateOf(true)
        private set

    var autoStartWorkout by mutableStateOf(false)
        private set

    fun toggleDarkTheme() {
        isDarkTheme = !isDarkTheme
    }

    fun toggleNotifications() {
        notificationsEnabled = !notificationsEnabled
    }
}
