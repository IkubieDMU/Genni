package com.example.genni

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.ui.theme.softLavender
import com.example.genni.viewmodels.AppSettingsViewModel
import com.example.genni.viewmodels.AuthViewModel

@Composable
fun SettingsScreen(nc: NavController, authViewModel: AuthViewModel, appSettingsViewModel: AppSettingsViewModel) {

    val isDarkMode = appSettingsViewModel.isDarkTheme
    val notifications = appSettingsViewModel.notificationsEnabled
    val metric = appSettingsViewModel.prefersMetric
    val autoStart = appSettingsViewModel.autoStartWorkout

    val context = LocalContext.current.applicationContext

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple, softLavender)))
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Settings", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(24.dp))

        /*SettingToggle("Dark Mode", isDarkMode) { appSettingsViewModel.toggleDarkTheme() }
        SettingToggle("Enable Notifications", notifications) { appSettingsViewModel.toggleNotifications() }*/

        //SettingToggle("Use Metric Units", metric) { appSettingsViewModel.toggleMetric() }
        //SettingToggle("Auto-start Workouts", autoStart) { appSettingsViewModel.toggleAutoStart() }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                authViewModel.logout {
                    nc.navigate(Screens.LoginScreen.screen)
                    Toast.makeText(context, "User Logged Out", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
        ) {
            Text("Logout", color = Color.White)
        }
    }
}


@Composable
fun SettingToggle(label: String, state: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.White, fontSize = 16.sp)
        Switch(checked = state, onCheckedChange = onToggle, colors = SwitchDefaults.colors(checkedThumbColor = emeraldGreen))
    }
}
