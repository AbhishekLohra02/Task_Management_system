package com.example.task_management_system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task_management_system.data.Task

// --- PROFESSIONAL COLOR PALETTE ---
private val managerPrimaryColor = Color(0xFF0D47A1)
private val managerSecondaryColor = Color(0xFF42A5F5)
private val managerBackgroundColor = Color(0xFFF5F7FA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagerDashboardScreen(
    onBack: () -> Unit,
    onCreateUser: () -> Unit,
    onCreateTeam: () -> Unit,
    onCreateTask: () -> Unit,
    onViewProgress: () -> Unit,
    onViewUserList: () -> Unit,
    onViewTeamList: () -> Unit,
    onViewTaskList: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        containerColor = managerBackgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Tasks:",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = managerPrimaryColor
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "MANAGER DASHBOARD",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = managerPrimaryColor,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .drawBehind {
                        val startColor = managerSecondaryColor
                        val endColor = Color.Transparent
                        drawRect(
                            brush = Brush.horizontalGradient(colors = listOf(startColor, endColor)),
                            topLeft = Offset(0f, size.height),
                            size = Size(size.width, 3.dp.toPx())
                        )
                    }
            )

            // Dropdown Menu Row: Go Button (Left) + Dropdown (Right)
            val options = listOf("User", "Team", "Task")
            var expanded by remember { mutableStateOf(false) }
            var selectedOptionText by remember { mutableStateOf(options[0]) }

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Bouton View à GAUCHE avec couleur secondaire
                Button(
                    onClick = {
                        when (selectedOptionText) {
                            "User" -> onViewUserList()
                            "Team" -> onViewTeamList()
                            "Task" -> onViewTaskList()
                        }
                    },
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = managerSecondaryColor)
                ) {
                    Text("View", fontWeight = FontWeight.Bold)
                }

                // Case du menu (un peu diminuée par weight(1f))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        readOnly = true,
                        value = selectedOptionText,
                        onValueChange = {},
                        label = { Text("View list of :") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                            focusedBorderColor = managerPrimaryColor,
                            unfocusedBorderColor = Color.LightGray,
                            focusedLabelColor = managerPrimaryColor
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        options.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    selectedOptionText = selectionOption
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }

            val buttonColors = ButtonDefaults.buttonColors(
                containerColor = managerPrimaryColor,
                contentColor = Color.White
            )

            ManagerActionCard("Create User", onCreateUser, buttonColors)
            Spacer(modifier = Modifier.height(16.dp))
            ManagerActionCard("Create Team", onCreateTeam, buttonColors)
            Spacer(modifier = Modifier.height(16.dp))
            ManagerActionCard("Create Task", onCreateTask, buttonColors)
            Spacer(modifier = Modifier.height(16.dp))
            ManagerActionCard("View Team Progress", onViewProgress, buttonColors)
        }
    }
}

@Composable
fun ManagerActionCard(text: String, onClick: () -> Unit, colors: ButtonColors) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = colors,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamProgressScreen(
    onBack: () -> Unit,
    tasks: List<Task> = emptyList() // Example data
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Team Progress Dashboard", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF0D47A1))
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Team_Id | User_Id | Task_Status", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            // Example Row
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text("Team: Alpha | User: 101 | Status: In Progress", modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManagerDashboardPreview() {
    ManagerDashboardScreen({}, {}, {}, {}, {}, {}, {}, {}, {})
}
