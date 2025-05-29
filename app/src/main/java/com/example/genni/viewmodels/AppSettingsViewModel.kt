package com.example.genni.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AppSettingsViewModel : ViewModel() {
    // Mutable state for tracking whether dark theme is enabled.
    // 'by mutableStateOf(false)' makes it an observable state holder for Compose UI.
    // 'private set' restricts modification to within this ViewModel, ensuring controlled updates.
    var isDarkTheme by mutableStateOf(false)
        private set

    // Mutable state for tracking whether notifications are enabled.
    // Defaults to true, meaning notifications are enabled by default.
    var notificationsEnabled by mutableStateOf(true)
        private set

    // Mutable state for tracking user's preference for metric units (vs. imperial).
    // Defaults to true, meaning metric units are preferred by default.
    var prefersMetric by mutableStateOf(true)
        private set

    // Mutable state for tracking whether workouts should auto-start.
    // Defaults to false, meaning auto-start is disabled by default.
    var autoStartWorkout by mutableStateOf(false)
        private set

    /**
     * Toggles the [isDarkTheme] state.
     * If dark theme is currently enabled, it disables it, and vice-versa.
     * This function is typically called when a user interacts with a "Dark Theme" toggle switch in the UI.
     */
    fun toggleDarkTheme() {
        isDarkTheme = !isDarkTheme
    }

    /**
     * Toggles the [notificationsEnabled] state.
     * If notifications are currently enabled, it disables them, and vice-versa.
     * This function is typically called when a user interacts with a "Notifications" toggle switch in the UI.
     */
    fun toggleNotifications() {
        notificationsEnabled = !notificationsEnabled
    }

    /**
     * Toggles the [prefersMetric] state.
     * If metric units are currently preferred, it switches to imperial, and vice-versa.
     * This function is typically called when a user interacts with a "Units" preference switch in the UI.
     */
    fun togglePrefeersMetric() {
        prefersMetric = !prefersMetric
    }

    /**
     * Toggles the [autoStartWorkout] state.
     * If auto-start workout is currently enabled, it disables it, and vice-versa.
     * This function is typically called when a user interacts with an "Auto-start Workout" toggle switch in the UI.
     */
    fun toggleAutoStartWorkout() {
        autoStartWorkout = !autoStartWorkout
    }
}
