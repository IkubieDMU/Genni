@file:Suppress("SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection",
    "SpellCheckingInspection", "SpellCheckingInspection", "SpellCheckingInspection"
)

package com.example.genni

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.genni.ui.theme.mintGreen
import com.example.genni.ui.theme.white

// Just a file for random useful composables

// Username & Password Composables
@Composable
fun MyCustomTF(value: String, updatedValue: (String) -> Unit, labelText: String, leading_Icon: ImageVector, iconDesc: String) {
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
        modifier = Modifier.width(335.dp).padding(bottom = 8.dp)
    )
}

@Composable
fun MyCustomPasswordTF(
    value: String,
    updatedValue: (String) -> Unit,
    labelText: String,
    leading_Icon: ImageVector,
    iconDesc: String
) {
    var passwordVisible by remember { mutableStateOf(false) } // Track visibility state

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
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                    tint = mintGreen
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
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
