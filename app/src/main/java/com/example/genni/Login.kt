package com.example.genni

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.genni.ui.theme.GenniTheme
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.ui.theme.mintGreen
import com.example.genni.ui.theme.royalPurple
import com.example.genni.ui.theme.softLavender
import com.example.genni.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(nc: NavController) {

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(emeraldGreen, royalPurple, softLavender))), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(16.dp))
            Text("Welcome Back to Genni", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = white)

            Spacer(modifier = Modifier.height(16.dp))

            // Mutable Variables
            var username by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            val context = LocalContext.current.applicationContext // For Toasts

            // Username TextField
            UsernameTF(username, {username = it}, "Username", Icons.Default.Person, "User Icon")

            Spacer(modifier = Modifier.height(10.dp))

            //Password TextField
            PasswordTF(password, {password = it}, "Password", Icons.Default.Lock, "Password Icon")

            Spacer(modifier = Modifier.height(10.dp))

            ClickableText("Forget Password?", softLavender,14.sp) {
                nc.navigate(Screens.ForgetPasswordScreen.screen) // Redirect to the "ForgetPassword" Screen
            }

            Spacer(modifier = Modifier.height(20.dp))

            //                  * Login Button *
            Button(
                onClick = {
                    //When Clicked, Authenticate User
                    authenticateUser(username,password,context) {
                        Toast.makeText(context,"Login Successful!",Toast.LENGTH_SHORT).show()
                        nc.navigate(Screens.HomeScreen.screen) {popUpTo(0)} // Redirect to the Home Screen
                    }
                          },
                colors = ButtonDefaults.buttonColors(containerColor = emeraldGreen),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login", color = white, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            ClickableText("Don't have an account? Sign Up", softLavender,14.sp) {
                nc.navigate(Screens.SignUpScreen.screen) // Redirect to the Sign up Screen

            }
        }
    }
}

//Sample Authentication Code
fun authenticateUser(username: String, password: String, context: Context, onLoginSuccess: () -> Unit) {

    var correctUsername = "admin"
    var correctPassword = "admin123"

    if(correctUsername == username.trim() && correctPassword == password.trim()) {
        onLoginSuccess() // Successful Login
    } else { //Invalid login credentials
        Toast.makeText(context,"Invalid Username or Password!!",Toast.LENGTH_SHORT).show()
    }
}

// Username & Password Composables
@Composable
fun UsernameTF(value: String, updatedValue: (String) -> Unit, labelText: String, leading_Icon: ImageVector, iconDesc: String) {
    OutlinedTextField(
        value = value,
        onValueChange = { updatedValue(it) },
        label = { Text(text = labelText)},
        shape = RoundedCornerShape(30.dp),
        colors = TextFieldDefaults.colors(
            focusedLeadingIconColor = mintGreen,
            unfocusedLeadingIconColor = mintGreen,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = mintGreen,
            unfocusedIndicatorColor = mintGreen,
            unfocusedPlaceholderColor = mintGreen,
            focusedTextColor = white,
            unfocusedTextColor = white,
            focusedLabelColor = mintGreen,
            unfocusedLabelColor = mintGreen
        ),
        leadingIcon = {
            Icon(imageVector = leading_Icon, contentDescription = iconDesc)
        },
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
    )
}

@Composable
fun PasswordTF(value: String, updatedValue: (String) -> Unit, labelText: String, leading_Icon: ImageVector, iconDesc: String) {
    OutlinedTextField(
        value = value,
        onValueChange = { updatedValue(it) },
        label = { Text(text = labelText)},
        shape = RoundedCornerShape(30.dp),
        colors = TextFieldDefaults.colors(
            focusedLeadingIconColor = mintGreen,
            unfocusedLeadingIconColor = mintGreen,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = mintGreen,
            unfocusedIndicatorColor = mintGreen,
            unfocusedPlaceholderColor = mintGreen,
            focusedTextColor = white,
            unfocusedTextColor = white,
            focusedLabelColor = mintGreen,
            unfocusedLabelColor = mintGreen
        ),
        leadingIcon = {
            Icon(imageVector = leading_Icon, contentDescription = iconDesc)
        },
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        visualTransformation = PasswordVisualTransformation() // For Password Hashing
    )
}
// When text is clicked, do something
@Composable
fun ClickableText(text: String, color: Color, fontsize: TextUnit, onClick: () -> Unit) {
    Text(
        text,
        color = color,
        fontSize = fontsize,
        modifier = Modifier.clickable{ onClick() }
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val nc = rememberNavController()
    GenniTheme { LoginScreen(nc) }
}
