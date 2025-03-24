package com.example.genni

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.genni.ui.theme.GenniTheme
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.ui.theme.softLavender
import com.example.genni.ui.theme.white
import com.example.genni.viewmodels.ForgetPasswordViewModel
import com.example.genni.viewmodels.ResetState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(nc: NavController, viewModel: ForgetPasswordViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple, softLavender))),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Forgot Password?", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = white)
            Spacer(modifier = Modifier.height(16.dp))

            UsernameTF(viewModel.email, { viewModel.onEmailChange(it) }, "Email", Icons.Default.Email, "Email Icon")
            Spacer(modifier = Modifier.height(20.dp))

            // Reset Password Button
            Button(
                onClick = { viewModel.resetPassword() },
                colors = ButtonDefaults.buttonColors(containerColor = emeraldGreen),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is ResetState.Loading
            ) {
                if (uiState is ResetState.Loading) {
                    CircularProgressIndicator(color = white, modifier = Modifier.size(20.dp))
                } else {
                    Text("Reset Password", color = white, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            ClickableText("Back to Login", softLavender, 14.sp) {
                nc.navigate(Screens.LoginScreen.screen)
            }

            LaunchedEffect(uiState) {
                when (uiState) {
                    is ResetState.Success -> {
                        Toast.makeText(context, (uiState as ResetState.Success).message, Toast.LENGTH_SHORT).show()
                    }
                    is ResetState.Error -> {
                        Toast.makeText(context, (uiState as ResetState.Error).message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun ForgetPasswordScreenPreview() {
//    GenniTheme {
//        val nc = rememberNavController()
//        ForgotPasswordScreen(nc)
//    }
//}