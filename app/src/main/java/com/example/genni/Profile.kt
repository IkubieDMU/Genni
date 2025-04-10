package com.example.genni

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.genni.ui.theme.GenniTheme
import com.example.genni.ui.theme.deepPurple
import com.example.genni.ui.theme.emeraldGreen
import com.example.genni.ui.theme.softLavender
import com.example.genni.ui.theme.white
import com.example.genni.viewmodels.AuthViewModel

@Composable
fun ProfileScreen(authViewModel: AuthViewModel) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(emeraldGreen, deepPurple, softLavender))).padding(24.dp)) {
        currentUser?.let { user ->
            Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
                Spacer(modifier = Modifier.height(32.dp))

                // Try parsing the profile picture URI and loading it
                val profilePicUri = user.profilePic?.let { Uri.parse(it) }

                // Load the image from URI using AsyncImage
                profilePicUri?.let {
                    AsyncImage(
                        model = ImageRequest.Builder(context).data(it).crossfade(true).build(),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(100.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)).border(2.dp, Color.White, CircleShape),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.userplaceholder), // Placeholder image
                        error = painterResource(R.drawable.userplaceholder)
                    )
                } ?: run {
                    // If no profile pic, show a placeholder image
                    Image(
                        painter = painterResource(R.drawable.userplaceholder),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(100.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f)).border(2.dp, Color.White, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "${user.firstName} ${user.middleName} ${user.lastName}",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("Username: ${user.username}", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
                Text("Email: ${user.email}", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp, color = white.copy(alpha = 0.3f))

                InfoRow("Age", "${user.age}")
                InfoRow("Gender", user.gender)
                InfoRow("Height", "${user.height} cm")
                InfoRow("Weight", "${user.weight} kg")
                InfoRow("Years of Training", "${user.yearsOfTraining}")
                InfoRow("Goals", user.goals.joinToString(", "))

                Spacer(modifier = Modifier.height(32.dp))
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No user data found", color = Color.White)
            }
        }
    }
}


@Composable
fun InfoRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color.White.copy(alpha = 0.05f), shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(text = label, color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
        Text(text = value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    GenniTheme { ProfileScreen(AuthViewModel()) }
}