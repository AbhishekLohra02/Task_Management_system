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
// Import your repository correctly
import com.example.task_management_system.data.repository.FirebaseRepository
import com.example.task_management_system.ui.theme.Task_Management_SystemTheme
import kotlinx.coroutines.launch

private val signinPrimaryColor = Color(0xFF0D47A1)
private val signinSecondaryColor = Color(0xFF42A5F5)
private val signinBackgroundColor = Color(0xFFF5F7FA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SigninScreen(onSignInSuccess: (String) -> Unit) {
    // --- DATA STATE (From Your Logic) ---
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val repository = remember { FirebaseRepository() } // Kept your repo
    val scope = rememberCoroutineScope()
    var isloading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        containerColor = signinBackgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ProjeManage", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = signinPrimaryColor)
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
            // Vic's UI Headers
            Text(text = "WELCOME BACK", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = signinPrimaryColor)
            Text(text = "Sign in to continue", color = Color.Gray, fontSize = 16.sp, modifier = Modifier.padding(bottom = 40.dp))

            // Vic's Outlined Style + Your State Binding
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Your Error Message Logic
            if (errorMessage.isNotBlank()) {
                Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Loading Indicator
            if (isloading) {
                CircularProgressIndicator(color = signinPrimaryColor)
            } else {
                Button(
                    onClick = {
                        // --- YOUR AUTH LOGIC ---
                        if (email.isBlank() || password.isBlank()) {
                            errorMessage = "Please enter email and password"
                            return@Button
                        }
                        isloading = true
                        errorMessage = ""
                        scope.launch {
                            val result = repository.login(email, password)
                            isloading = false
                            if (result.isSuccess) {
                                val user = result.getOrNull()
                                if (user != null) {
                                    onSignInSuccess(user.role)
                                } else {
                                    errorMessage = "User not found"
                                }
                            } else {
                                errorMessage = result.exceptionOrNull()?.message ?: "Login failed"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = signinPrimaryColor)
                ) {
                    Text("SIGN IN", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SigninScreenPreview() {
    MaterialTheme {
        SigninScreen(onSignInSuccess = {})
    }
}