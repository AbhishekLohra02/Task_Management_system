package com.example.task_management_system

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class MemberProgress(
    val name: String,
    val task: String,
    val status: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerDashboardScreen(
    onLogout: () -> Unit
) {
    var showProgress by remember { mutableStateOf(false) }

    val members = listOf(
        MemberProgress("Abhishek", "UI Design", "Completed"),
        MemberProgress("Rohit", "API Development", "In Progress"),
        MemberProgress("Virat", "Testing", "Pending")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Welcome Manager") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { padding ->
        if (showProgress) {
            TeamProgressScreen(
                members,
                Modifier.padding(padding),
                onBack = { showProgress = false }
            )
        } else {
            ManagerMenuScreen(
                Modifier.padding(padding),
                onCreateUser = {},
                onCreateTask = {},
                onViewProgress = { showProgress = true }
            )
        }
    }
}

@Composable
fun ManagerMenuScreen(
    modifier: Modifier = Modifier,
    onCreateUser: () -> Unit,
    onCreateTask: () -> Unit,
    onViewProgress: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onCreateUser, modifier = Modifier.fillMaxWidth()) {
            Text("Create User")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onCreateTask, modifier = Modifier.fillMaxWidth()) {
            Text("Create Task")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onViewProgress, modifier = Modifier.fillMaxWidth()) {
            Text("View Team Progress")
        }
    }
}

@Composable
fun TeamProgressScreen(
    list: List<MemberProgress>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onBack) { Text("Back") }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(list) { user ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(user.name, fontWeight = FontWeight.Bold)
                        Text("Task: ${user.task}")
                        Text("Status: ${user.status}")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManagerDashboardPreview() {
    ManagerDashboardScreen(onLogout = {})
}
