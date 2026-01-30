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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.task_management_system.data.User
import com.example.task_management_system.viewmodel.ManagerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(
    onBack: () -> Unit,
    onUserCreated: () -> Unit,
    viewModel: ManagerViewModel = viewModel()
) {
    var userName by remember { mutableStateOf("") }
    var userSurname by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }
    var userAddress by remember { mutableStateOf("") }
    var teamId by remember { mutableStateOf("") }

    val operationStatus by viewModel.operationStatus.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(operationStatus) {
        operationStatus?.let { result ->
            if (result.isSuccess) {
                scope.launch { snackbarHostState.showSnackbar("User created successfully") }
                viewModel.clearStatus()
                onUserCreated()
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
                title = { Text("Create User Dashboard", color = Color.White, fontWeight = FontWeight.Bold) },
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
            OutlinedTextField(value = userName, onValueChange = { userName = it }, label = { Text("First Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = userSurname, onValueChange = { userSurname = it }, label = { Text("Surname") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = userEmail, onValueChange = { userEmail = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = userPassword, 
                onValueChange = { userPassword = it }, 
                label = { Text("Password") }, 
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            OutlinedTextField(value = userAddress, onValueChange = { userAddress = it }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth())
            
            // Team ID Dropdown
            var expanded by remember { mutableStateOf(false) }
            val teams by viewModel.teams.collectAsState()
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    value = teams.find { it.id == teamId }?.name ?: "Select Team",
                    onValueChange = {},
                    label = { Text("Team") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    teams.forEach { team ->
                        DropdownMenuItem(
                            text = { Text(team.name) },
                            onClick = {
                                teamId = team.id
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
                        if (userEmail.isNotBlank() && userPassword.isNotBlank() && userName.isNotBlank()) {
                            val newUser = User(
                                name = userName,
                                surname = userSurname,
                                email = userEmail,
                                address = userAddress,
                                role = "User",
                                teamId = teamId
                            )
                            viewModel.createUser(newUser, userPassword)
                        } else {
                            scope.launch { snackbarHostState.showSnackbar("Please fill required fields") }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                    enabled = !isLoading
                ) {
                    Text("Save User", fontSize = 18.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateUserPreview() {
    CreateUserScreen({}, {})
}
