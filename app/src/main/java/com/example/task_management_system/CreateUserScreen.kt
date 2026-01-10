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
fun CreateUserScreen(
    onBack: () -> Unit,
    onUserCreated: () -> Unit
) {
    var userId by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var userSurname by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userAddress by remember { mutableStateOf("") }
    var team by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create User Dashboard", color = Color.White, fontWeight = FontWeight.Bold) },
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
            OutlinedTextField(value = userId, onValueChange = { userId = it }, label = { Text("User Id (Integer)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = userName, onValueChange = { userName = it }, label = { Text("User Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = userSurname, onValueChange = { userSurname = it }, label = { Text("User Surname") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = userEmail, onValueChange = { userEmail = it }, label = { Text("User Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = userAddress, onValueChange = { userAddress = it }, label = { Text("User Address") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = team, onValueChange = { team = it }, label = { Text("Team") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("user saved")
                        delay(1000)
                        onUserCreated()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
            ) {
                Text("Save User", fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateUserPreview() {
    CreateUserScreen({}, {})
}
