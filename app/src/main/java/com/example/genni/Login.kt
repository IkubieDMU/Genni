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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.genni.states.AuthState
import com.example.genni.ui.theme.GenniTheme
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.ui.theme.softLavender
import com.example.genni.ui.theme.white
import com.example.genni.viewmodels.AuthViewModel
import com.example.genni.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(nc: NavController, authViewModel: AuthViewModel, userViewModel: UserViewModel) {
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple, deepPurple))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Welcome Back to Genni", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = white)

            Spacer(modifier = Modifier.height(16.dp))

            // Username TextField
            MyCustomTF(
                value = authViewModel.username,
                updatedValue = { authViewModel.onUsernameChange(it) },
                labelText = "Username",
                leading_Icon = Icons.Default.Person,
                iconDesc = "User Icon"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Password TextField
            MyCustomPasswordTF(
                value = authViewModel.password,
                updatedValue = { authViewModel.onPasswordChange(it) },
                labelText = "Password",
                leading_Icon = Icons.Default.Lock,
                iconDesc = "Password Icon"
            )

            Spacer(modifier = Modifier.height(10.dp))

            ClickableText(text = "Forget Password?", color = softLavender, fontsize = 14.sp,
                onClick = { nc.navigate(Screens.ForgetPasswordScreen.screen) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Login Button
            Button(
                onClick = {
                    authViewModel.authenticateUser(userViewModel) {
                        nc.navigate(Screens.HomeScreen.screen) { popUpTo(0) }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = emeraldGreen),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth(),
                enabled = authState !is AuthState.Loading
            ) {
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator(color = white, modifier = Modifier.size(20.dp))
                } else {
                    Text("Login", color = white, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            ClickableText(
                "Don't have an account? Sign Up", color = softLavender, fontsize = 14.sp,
                onClick = { nc.navigate(Screens.SignUpScreen.screen) }
            )

            LaunchedEffect(authState) {
                when (authState) {
                    is AuthState.Success -> {
                        Toast.makeText(context, (authState as AuthState.Success).message, Toast.LENGTH_SHORT).show()
                    }
                    is AuthState.Error -> {
                        Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val nc = rememberNavController()
    GenniTheme { LoginScreen(nc, AuthViewModel(),UserViewModel()) }
}
