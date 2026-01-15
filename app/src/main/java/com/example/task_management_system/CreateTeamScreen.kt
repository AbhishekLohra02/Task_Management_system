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
import com.example.task_management_system.viewmodel.SuperAdminViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamScreen(
    onBack: () -> Unit,
    onTeamCreated: () -> Unit,
    viewModel: SuperAdminViewModel = viewModel()
) {
    var teamName by remember { mutableStateOf("") }
    var teamDescription by remember { mutableStateOf("") }
    var managerId by remember { mutableStateOf("") }

    val operationStatus by viewModel.operationStatus.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val managers by viewModel.managers.collectAsState()
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(operationStatus) {
        operationStatus?.let { result ->
            if (result.isSuccess) {
                scope.launch { snackbarHostState.showSnackbar("Team created successfully") }
                viewModel.clearStatus()
                onTeamCreated()
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
                title = { Text("Team Creation Dashboard", color = Color.White, fontWeight = FontWeight.Bold) },
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
            OutlinedTextField(value = teamName, onValueChange = { teamName = it }, label = { Text("Team Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = teamDescription, onValueChange = { teamDescription = it }, label = { Text("Team Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

            // Dropdown for Manager selection
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    value = managers.find { it.uid == managerId }?.name ?: "Select Manager",
                    onValueChange = {},
                    label = { Text("Assign Manager") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    managers.forEach { manager ->
                        DropdownMenuItem(
                            text = { Text(manager.name) },
                            onClick = {
                                managerId = manager.uid
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
                        if (teamName.isNotBlank() && managerId.isNotBlank()) {
                            viewModel.createTeam(teamName, managerId)
                        } else {
                            scope.launch { snackbarHostState.showSnackbar("Please fill required fields") }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                    enabled = !isLoading
                ) {
                    Text("Save Team", fontSize = 18.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTeamPreview() {
    CreateTeamScreen(onBack = {}, onTeamCreated = {})
}
