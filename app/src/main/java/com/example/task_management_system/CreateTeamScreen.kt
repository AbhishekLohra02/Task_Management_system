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
fun CreateTeamScreen(
    onBack: () -> Unit,
    onTeamCreated: () -> Unit
) {
    var teamId by remember { mutableStateOf("") }
    var teamName by remember { mutableStateOf("") }
    var teamDescription by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Team Creation Dashboard", color = Color.White, fontWeight = FontWeight.Bold) },
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
            OutlinedTextField(value = teamId, onValueChange = { teamId = it }, label = { Text("Team Id") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = teamName, onValueChange = { teamName = it }, label = { Text("Team Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = teamDescription, onValueChange = { teamDescription = it }, label = { Text("Team Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("team saved")
                        delay(1000)
                        onTeamCreated()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
            ) {
                Text("Save Team", fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTeamPreview() {
    CreateTeamScreen(onBack = {}, onTeamCreated = {})
}
