package com.example.task_management_system

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.task_management_system.data.Task
import com.example.task_management_system.viewmodel.ManagerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    onBack: () -> Unit,
    onTaskCreated: () -> Unit,
    viewModel: ManagerViewModel = viewModel()
) {
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var assignedToUserId by remember { mutableStateOf("") }

    val operationStatus by viewModel.operationStatus.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val users by viewModel.users.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(operationStatus) {
        operationStatus?.let { result ->
            if (result.isSuccess) {
                scope.launch { snackbarHostState.showSnackbar("Task created successfully") }
                viewModel.clearStatus()
                onTaskCreated()
            } else {
                scope.launch { snackbarHostState.showSnackbar("Error: ${result.exceptionOrNull()?.message}") }
                viewModel.clearStatus()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Task Creation Dashboard", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = taskTitle, onValueChange = { taskTitle = it }, label = { Text("Task Title") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = taskDescription, onValueChange = { taskDescription = it }, label = { Text("Task Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

            // Dropdown for User selection
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    value = users.find { it.uid == assignedToUserId }?.name ?: "Select User",
                    onValueChange = {},
                    label = { Text("Assign To") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    users.forEach { user ->
                        DropdownMenuItem(
                            text = { Text(user.name) },
                            onClick = {
                                assignedToUserId = user.uid
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            
            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFF0D47A1))
            } else {
                Button(
                    onClick = {
                        if (taskTitle.isNotBlank() && assignedToUserId.isNotBlank()) {
                            val newTask = Task(
                                title = taskTitle,
                                description = taskDescription,
                                assignedTo = assignedToUserId,
                                status = "To Do"
                            )
                            viewModel.createTask(newTask)
                        } else {
                            scope.launch { snackbarHostState.showSnackbar("Please fill required fields") }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                    enabled = !isLoading
                ) {
                    Text("Save Task", fontSize = 18.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTaskPreview() {
    CreateTaskScreen(onBack = {}, onTaskCreated = {})
}
