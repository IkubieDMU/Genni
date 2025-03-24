package com.example.genni

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.genni.ui.theme.mintGreen
import com.example.genni.ui.theme.white

// Just a file for random useful composables

// Username & Password Composables
@Composable
fun UsernameTF(value: String, updatedValue: (String) -> Unit, labelText: String, leading_Icon: ImageVector, iconDesc: String) {
    OutlinedTextField(
        value = value,
        onValueChange = { updatedValue(it) },
        label = { Text(text = labelText) },
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
        label = { Text(text = labelText) },
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