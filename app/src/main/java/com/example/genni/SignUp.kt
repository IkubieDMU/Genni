package com.example.genni

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.genni.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(nc: NavController) {

    val context = LocalContext.current.applicationContext

    Box(
        modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple, softLavender))),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(16.dp))
            Text("Create Your Genni Account", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = white)

            Spacer(modifier = Modifier.height(16.dp))

            var name by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            MyCustomTF(name, { name = it }, "Full Name", Icons.Default.Person, "Name Icon")
            Spacer(modifier = Modifier.height(10.dp))
            MyCustomTF(email, { email = it }, "Email", Icons.Default.Email, "Email Icon")
            Spacer(modifier = Modifier.height(10.dp))
            MyCustomPasswordTF(password, { password = it }, "Password", Icons.Default.Lock, "Password Icon")
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { Toast.makeText(context,"New User Created!!",Toast.LENGTH_SHORT).show() },
                colors = ButtonDefaults.buttonColors(containerColor = emeraldGreen),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up", color = white, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            ClickableText("Already have an account? Login", softLavender, 14.sp) {
                nc.navigate(Screens.LoginScreen.screen)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    val nc = rememberNavController()
    GenniTheme { SignUpScreen(nc) }
}
