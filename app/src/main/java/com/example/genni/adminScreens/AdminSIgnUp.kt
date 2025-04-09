package com.example.genni.adminScreens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.genni.MyCustomPasswordTF
import com.example.genni.MyCustomTF
import com.example.genni.Screens
import com.example.genni.models.Admin
import com.example.genni.ui.theme.GenniTheme
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.ui.theme.white
import com.example.genni.viewmodels.AdminViewModel

@Composable
fun AdminSignUpScreen(nc: NavController, adminViewModel: AdminViewModel) {
    // User Input Variables (Default Value --> Empty String)
    var username by remember{ mutableStateOf("") }
    var password by remember{ mutableStateOf("") }

    val context = LocalContext.current.applicationContext // For Toasts
    val scrollState = rememberScrollState() //Scroll functionality

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple, deepPurple)))
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text("Create a new Genni Account", fontSize = 28.sp, fontWeight = FontWeight.SemiBold, color = white)
        Spacer(modifier = Modifier.height(10.dp))

        MyCustomTF(username, { username = it }, "Username", Icons.Default.Person, "First Name Icon")
        Spacer(modifier = Modifier.height(10.dp))
        MyCustomPasswordTF(password, { password = it }, "Password", Icons.Default.Lock, "Password Icon")
        Spacer(modifier = Modifier.height(10.dp))

        // Sign Up Button
        Button(
            onClick = {
                var admin = Admin(0,username,password)
                adminViewModel.registerAdmin(
                    admin, /*newly created Admin Object*/
                    onSuccess = {
                        Toast.makeText(context, "Admin Registered Successfully", Toast.LENGTH_SHORT).show() // Display toast message
                        nc.navigate(Screens.AdminLoginScreen.screen) // Redirect to the Admin's Login Page
                    },
                    onFailure = { error ->
                        Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
                    }
                )
            }
        ) {
            Text("Sign Up")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminSignUpScreenPreview() {
    val nc = rememberNavController()
    GenniTheme { AdminSignUpScreen(nc, AdminViewModel()) }
}