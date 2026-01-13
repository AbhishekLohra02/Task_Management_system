package com.example.task_management_system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.task_management_system.data.repository.FirebaseRepository
import com.example.task_management_system.ui.theme.Task_Management_SystemTheme
import kotlinx.coroutines.launch

@Composable
fun SigninScreen(onSignInSuccess: (String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val repository = FirebaseRepository()
    val scope = rememberCoroutineScope()
    var isloading by remember {mutableStateOf(false)}
    var errorMessage by remember {mutableStateOf(" ")}


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Sign In",
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email (admin, manager, or other)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage != null) {
            Text(text = errorMessage, color = Color.Red)}
        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Please enter email and password"
                    return@Button
                }
                isloading = true
                errorMessage = " "
                scope.launch {
                    val result = repository.login(email, password)
                    isloading = false

                    if(result.isSuccess) {
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
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Sign In")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SigninScreenPreview() {
    Task_Management_SystemTheme {
        SigninScreen(onSignInSuccess = {})
    }
}
