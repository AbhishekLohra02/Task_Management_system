package com.example.task_management_system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task_management_system.ui.theme.Task_Management_SystemTheme

// --- PROFESSIONAL COLOR PALETTE ---
private val signinPrimaryColor = Color(0xFF0D47A1)
private val signinSecondaryColor = Color(0xFF42A5F5)
private val signinBackgroundColor = Color(0xFFF5F7FA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SigninScreen(onSignInSuccess: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        containerColor = signinBackgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "ProjeManage",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = signinPrimaryColor
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "WELCOME BACK",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = signinPrimaryColor,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .drawBehind {
                        val startColor = signinSecondaryColor
                        val endColor = Color.Transparent
                        drawRect(
                            brush = Brush.horizontalGradient(colors = listOf(startColor, endColor)),
                            topLeft = Offset(0f, size.height),
                            size = Size(size.width, 3.dp.toPx())
                        )
                    }
            )
            
            Text(
                text = "Sign in to continue",
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = signinPrimaryColor,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = signinPrimaryColor,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val role = when {
                        email.contains("admin", ignoreCase = true) -> "SuperAdmin"
                        email.contains("manager", ignoreCase = true) -> "Manager"
                        else -> "User"
                    }
                    onSignInSuccess(role)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = signinPrimaryColor)
            ) {
                Text("SIGN IN", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SigninScreenPreview() {
    // Si le thème cause toujours une erreur, utilisez simplement MaterialTheme {} pour la preview
    MaterialTheme {
        SigninScreen(onSignInSuccess = {})
    }
}
