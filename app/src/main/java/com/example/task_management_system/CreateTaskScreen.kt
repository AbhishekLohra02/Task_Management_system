package com.example.task_management_system

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    onBack: () -> Unit,
    onTaskCreated: () -> Unit
) {
    var taskId by remember { mutableStateOf("") }
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Task Creation Dashboard", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF0D47A1))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(value = taskId, onValueChange = { taskId = it }, label = { Text("Task Id") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = taskName, onValueChange = { taskName = it }, label = { Text("Task Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = taskDescription, onValueChange = { taskDescription = it }, label = { Text("Task Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onTaskCreated,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
            ) {
                Text("Save Task", fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTaskPreview() {
    CreateTaskScreen(onBack = {}, onTaskCreated = {})
}
