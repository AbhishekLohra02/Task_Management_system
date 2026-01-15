package com.example.task_management_system

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.task_management_system.data.Team
import com.example.task_management_system.viewmodel.SuperAdminViewModel

private val adminPrimaryColor = Color(0xFF0D47A1)
private val adminSecondaryColor = Color(0xFF42A5F5)
private val adminBackgroundColor = Color(0xFFF5F7FA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminScreen(
    onBack: () -> Unit,
    onCreateManager: () -> Unit,
    onDeleteManager: () -> Unit,
    onLogout: () -> Unit,
    viewModel: SuperAdminViewModel = viewModel()
) {
    var screenState by remember { mutableStateOf("dashboard") }
    var selectedTeam by remember { mutableStateOf<Team?>(null) }
    
    val teams by viewModel.teams.collectAsState()

    Scaffold(
        containerColor = adminBackgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tasks:", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    TextButton(onClick = onLogout) { Text("Logout", color = Color.White) }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = adminPrimaryColor)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp)) {
            when (screenState) {
                "dashboard" -> {
                    SuperAdminDashboardContent(
                        onCreateManager = onCreateManager,
                        onDeleteManager = onDeleteManager,
                        onViewTeams = { screenState = "teams" }
                    )
                }
                "teams" -> {
                    AdminTeamListScreen(
                        teams = teams,
                        onTeamClick = { team ->
                            selectedTeam = team
                            screenState = "teamDetail"
                        },
                        onBackToDashboard = { screenState = "dashboard" }
                    )
                }
                "teamDetail" -> {
                    AdminTeamDetailScreen(
                        team = selectedTeam!!,
                        onBack = { screenState = "teams" }
                    )
                }
            }
        }
    }
}

@Composable
fun SuperAdminDashboardContent(
    onCreateManager: () -> Unit,
    onDeleteManager: () -> Unit,
    onViewTeams: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "ADMIN DASHBOARD",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = adminPrimaryColor,
            modifier = Modifier.padding(bottom = 40.dp).drawBehind {
                drawRect(
                    brush = Brush.horizontalGradient(colors = listOf(adminSecondaryColor, Color.Transparent)),
                    topLeft = Offset(0f, size.height),
                    size = Size(size.width, 3.dp.toPx())
                )
            }
        )

        val buttonColors = ButtonDefaults.buttonColors(containerColor = adminPrimaryColor)

        AdminActionCard("Create Manager", onCreateManager, buttonColors)
        Spacer(modifier = Modifier.height(16.dp))
        AdminActionCard("Delete Manager", onDeleteManager, buttonColors)
        Spacer(modifier = Modifier.height(16.dp))
        AdminActionCard("View All Teams", onViewTeams, buttonColors)
    }
}

@Composable
fun AdminActionCard(text: String, onClick: () -> Unit, colors: ButtonColors) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = colors,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun AdminTeamListScreen(teams: List<Team>, onTeamClick: (Team) -> Unit, onBackToDashboard: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(vertical = 24.dp)) {
        TextButton(onClick = onBackToDashboard) { Text("< Dashboard", color = adminSecondaryColor) }
        Text("TEAM OVERVIEW", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = adminPrimaryColor, modifier = Modifier.padding(vertical = 16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(teams) { team ->
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    ListItem(
                        headlineContent = { Text(team.name, fontWeight = FontWeight.Bold) },
                        supportingContent = { Text(team.description) },
                        trailingContent = {
                            Button(onClick = { onTeamClick(team) }, colors = ButtonDefaults.buttonColors(containerColor = adminSecondaryColor)) {
                                Text("Details")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminTeamDetailScreen(team: Team, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(vertical = 32.dp)) {
        TextButton(onClick = onBack) { Text("< Back to Teams", color = adminSecondaryColor) }
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(team.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = adminPrimaryColor)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Description:", fontWeight = FontWeight.Bold, color = Color.Gray)
                Text(team.description, fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SuperAdminPreview() {
    SuperAdminScreen({}, {}, {}, {})
}
