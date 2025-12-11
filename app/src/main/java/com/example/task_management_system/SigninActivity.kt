package com.example.task_management_system

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.task_management_system.ui.theme.Task_Management_SystemTheme

class SigninActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Task_Management_SystemTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "signin"
                ) {
                    composable("signin") {
                        SigninPageContent(
                            onSignInClick = {
                                navController.navigate("managerDashboard")
                            }
                        )
                    }

                    // Manager dashboard
                    composable("managerDashboard") {
                        ManagerDashboardScreen(

                        )


                    }
                }
            }
        }
    }
}

@Composable
fun SigninPageContent(
    onSignInClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSignInClick() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Sign In")
        }
    }
}
