package com.example.genni

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.viewmodels.ForgetPasswordViewModel

@Composable
fun FPContd(fpViewModel: ForgetPasswordViewModel, nc: NavController) {
    var newPassword by remember { mutableStateOf("")}
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Validation Logic
    fun isValidPassword(password: String): Boolean { // Return true/false
        val lengthValid = password.length >= 8
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasSymbol = password.any { !it.isLetterOrDigit() }

        return lengthValid && hasUpperCase && hasSymbol // Check if all 3 conditions are true
    }

    Column(modifier = Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Reset Your Password", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        MyCustomTF(
            value = newPassword,
            updatedValue = { newPassword = it },
            labelText = "New Password",
            leading_Icon = Icons.Default.Lock,
            iconDesc = "New Password"
        )

        Spacer(modifier = Modifier.height(10.dp))

        MyCustomTF(
            value = confirmPassword,
            updatedValue = { confirmPassword = it },
            labelText = "Confirm Password",
            leading_Icon = Icons.Default.Lock,
            iconDesc = "Confirm Password"
        )
        Spacer(modifier = Modifier.height(20.dp))

        error?.let {
            Text(it, color = Color.Red, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(10.dp))
        }

        Button(
            onClick = {
                when {
                    newPassword != confirmPassword -> {
                        error = "Passwords do not match"
                    }
                    !isValidPassword(newPassword) -> {
                        error = "Password must be at least 8 characters,\ncontain 1 uppercase letter and 1 symbol"
                    }
                    else -> {
                        fpViewModel.updatePassword(newPassword) { success ->
                            if (success) {
                                Toast.makeText(context, "Password updated successfully", Toast.LENGTH_LONG).show()
                                nc.navigate(Screens.LoginScreen.screen) {
                                    popUpTo(Screens.FPContdScreen.screen) { inclusive = true }
                                }
                            } else {
                                error = "Failed to update password. Try again."
                            }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = emeraldGreen)
        ) {
            Text("Update Password", color = Color.White)
        }
    }
}
