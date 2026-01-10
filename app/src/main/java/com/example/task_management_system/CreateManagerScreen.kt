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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateManagerScreen(
    onBack: () -> Unit,
    onManagerCreated: () -> Unit
) {
    var managerId by remember { mutableStateOf("") }
    var managerName by remember { mutableStateOf("") }
    var managerSurname by remember { mutableStateOf("") }
    var managerEmail by remember { mutableStateOf("") }
    var managerAddress by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create Manager Dashboard", color = Color.White, fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(value = managerId, onValueChange = { managerId = it }, label = { Text("Manager Id") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = managerName, onValueChange = { managerName = it }, label = { Text("Manager Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = managerSurname, onValueChange = { managerSurname = it }, label = { Text("Manager Surname") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = managerEmail, onValueChange = { managerEmail = it }, label = { Text("Manager Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = managerAddress, onValueChange = { managerAddress = it }, label = { Text("Manager Address") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("manager saved")
                        delay(1000)
                        onManagerCreated()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
            ) {
                Text("Save", fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateManagerPreview() {
    CreateManagerScreen(onBack = {}, onManagerCreated = {})
}
