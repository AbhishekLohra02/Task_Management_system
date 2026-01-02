package com.example.task_management_system

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.task_management_system.ui.theme.Task_Management_SystemTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task_Management_SystemTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onTimeout = {
                navController.navigate("signin") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }
        composable("signin") {
            SigninScreen(onSignInSuccess = { role ->
                when (role) {
                    "SuperAdmin" -> navController.navigate("superAdminDashboard") {
                        popUpTo("signin") { inclusive = true }
                    }
                    "Manager" -> navController.navigate("managerDashboard") {
                        popUpTo("signin") { inclusive = true }
                    }
                    "User" -> navController.navigate("teamList") {
                        popUpTo("signin") { inclusive = true }
                    }
                }
            })
        }
        composable("superAdminDashboard") {
            SuperAdminScreen(onLogout = {
                navController.navigate("signin") {
                    popUpTo("superAdminDashboard") { inclusive = true }
                }
            })
        }
        composable("managerDashboard") {
            ManagerDashboardScreen(onLogout = {
                navController.navigate("signin") {
                    popUpTo("managerDashboard") { inclusive = true }
                }
            })
        }
        composable("teamList") {
            TeamListScreen(
                onTeamSelected = { team ->
                    navController.navigate("taskDetail")
                },
                onLogout = {
                    navController.navigate("signin") {
                        popUpTo("teamList") { inclusive = true }
                    }
                }
            )
        }
        composable("taskDetail") {
            TaskDetailScreen(onLogout = {
                navController.navigate("signin") {
                    popUpTo("taskDetail") { inclusive = true }
                }
            })
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(key1 = true) {
        delay(5000) // Delay from your SplashScreen.kt
        onTimeout()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("ProjecManag", fontSize = 48.sp)
    }
}
