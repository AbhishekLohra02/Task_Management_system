package com.example.task_management_system

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.task_management_system.data.Team
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
            SuperAdminScreen(
                onBack = { 
                    navController.navigate("signin") {
                        popUpTo("superAdminDashboard") { inclusive = true }
                    }
                },
                onCreateManager = { navController.navigate("createManager") },
                onDeleteManager = { navController.navigate("listeManager") },
                onViewTeamList = { navController.navigate("teamList") },
                onLogout = {
                    navController.navigate("signin") {
                        popUpTo("superAdminDashboard") { inclusive = true }
                    }
                }
            )
        }
        composable("createManager") {
            CreateManagerScreen(
                onBack = { navController.popBackStack() },
                onManagerCreated = { navController.popBackStack() }
            )
        }
        composable("listeManager") {
            ListeManagerScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("managerDashboard") {
            ManagerDashboardScreen(
                onBack = { 
                    navController.navigate("signin") {
                        popUpTo("managerDashboard") { inclusive = true }
                    }
                },
                onCreateUser = { navController.navigate("createUser") },
                onCreateTeam = { navController.navigate("createTeam") },
                onCreateTask = { navController.navigate("createTask") },
                onViewProgress = { navController.navigate("viewProgress") },
                onViewUserList = { navController.navigate("userList") },
                onViewTeamList = { navController.navigate("teamList") },
                onViewTaskList = { navController.navigate("taskList") },
                onLogout = {
                    navController.navigate("signin") {
                        popUpTo("managerDashboard") { inclusive = true }
                    }
                }
            )
        }
        composable("createUser") {
            CreateUserScreen(
                onBack = { navController.popBackStack() },
                onUserCreated = { navController.popBackStack() }
            )
        }
        composable("createTeam") {
            CreateTeamScreen(
                onBack = { navController.popBackStack() },
                onTeamCreated = { navController.popBackStack() }
            )
        }
        composable("createTask") {
            CreateTaskScreen(
                onBack = { navController.popBackStack() },
                onTaskCreated = { navController.popBackStack() }
            )
        }
        composable("viewProgress") {
            TeamProgressScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("userList") {
            UserListScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("taskList") {
            TaskListScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("teamList") {
            TeamListScreen(
                onBack = { 
                    navController.popBackStack()
                },
                onTeamSelected = { team ->
                    navController.navigate("userTasks/${team.id}")
                },
                onLogout = {
                    navController.navigate("signin") {
                        popUpTo("teamList") { inclusive = true }
                    }
                }
            )
        }
        composable(
            "userTasks/{teamId}",
            arguments = listOf(navArgument("teamId") { type = NavType.StringType })
        ) { backStackEntry ->
            val teamId = backStackEntry.arguments?.getString("teamId") ?: ""
            UserTasksScreen(
                teamId = teamId,
                onBack = {
                    navController.navigate("teamList") {
                        popUpTo("userTasks/$teamId") { inclusive = true }
                    }
                },
                onTaskSelected = { taskId ->
                    navController.navigate("taskDetail/$taskId")
                },
                onLogout = {
                    navController.navigate("signin") {
                        popUpTo("userTasks/$teamId") { inclusive = true }
                    }
                }
            )
        }
        composable(
            "taskDetail/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            TaskDetailScreen(
                taskId = taskId,
                onBack = { 
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate("signin") {
                        popUpTo("taskDetail/$taskId") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(key1 = true) {
        delay(3000)
        onTimeout()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF0D47A1)),
        contentAlignment = Alignment.Center
    ) {
        Text("ProjecManag", fontSize = 40.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}
