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
import com.example.task_management_system.viewmodel.SuperAdminViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateManagerScreen(
    onBack: () -> Unit,
    onManagerCreated: () -> Unit,
    viewModel: SuperAdminViewModel = viewModel()
) {
    var managerName by remember { mutableStateOf("") }
    var managerSurname by remember { mutableStateOf("") }
    var managerEmail by remember { mutableStateOf("") }
    var managerPassword by remember { mutableStateOf("") }
    var managerAddress by remember { mutableStateOf("") }

    val operationStatus by viewModel.operationStatus.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Observe operation status to navigate back or show error
    LaunchedEffect(operationStatus) {
        operationStatus?.let { result ->
            if (result.isSuccess) {
                scope.launch {
                    snackbarHostState.showSnackbar("Manager created successfully")
                }
                viewModel.clearStatus()
                onManagerCreated()
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar("Error: ${result.exceptionOrNull()?.message}")
                }
                viewModel.clearStatus()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create Manager", color = Color.White, fontWeight = FontWeight.Bold) },
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
            OutlinedTextField(value = managerName, onValueChange = { managerName = it }, label = { Text("First Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = managerSurname, onValueChange = { managerSurname = it }, label = { Text("Surname") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = managerEmail, onValueChange = { managerEmail = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = managerPassword, 
                onValueChange = { managerPassword = it }, 
                label = { Text("Password") }, 
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            OutlinedTextField(value = managerAddress, onValueChange = { managerAddress = it }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.weight(1f))
            
            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFF0D47A1))
            } else {
                Button(
                    onClick = {
                        if (managerEmail.isNotBlank() && managerPassword.isNotBlank() && managerName.isNotBlank()) {
                            viewModel.createManager(managerName, managerEmail, managerPassword)
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Please fill required fields")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                    enabled = !isLoading
                ) {
                    Text("Create Manager", fontSize = 18.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateManagerPreview() {
    CreateManagerScreen(onBack = {}, onManagerCreated = {})
}
