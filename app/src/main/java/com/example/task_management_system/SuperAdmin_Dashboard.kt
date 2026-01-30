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
    onViewTeamList: () -> Unit,
    onLogout: () -> Unit,
    viewModel: SuperAdminViewModel = viewModel()
) {
    Scaffold(
        containerColor = adminBackgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tasks:", color = Color.White, fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(onClick = onLogout) { Text("Logout", color = Color.White) }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = adminPrimaryColor)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp)) {
            SuperAdminDashboardContent(
                onCreateManager = onCreateManager,
                onDeleteManager = onDeleteManager,
                onViewTeams = onViewTeamList
            )
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

@Preview(showBackground = true)
@Composable
fun SuperAdminPreview() {
    SuperAdminScreen({}, {}, {}, {}, {})
}
