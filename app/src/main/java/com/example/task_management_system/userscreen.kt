package com.example.task_management_system

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- PALETTE DE COULEURS ---
val userScreenPrimaryColor = Color(0xFF0D47A1)      // Bleu foncé
val userScreenSecondaryColor = Color(0xFF42A5F5)    // Bleu clair
val userScreenBackgroundColor = Color(0xFFF5F5F5)   // Fond gris clair
val userScreenOnPrimaryColor = Color.White        // Texte sur fond bleu
val userScreenTextColor = Color(0xFF212121)         // Texte principal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserTasksScreen(
    onBack: () -> Unit,
    onTaskSelected: (String) -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "ProjeManage",
                        color = userScreenOnPrimaryColor,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = userScreenOnPrimaryColor
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("LogOut", color = userScreenOnPrimaryColor)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = userScreenPrimaryColor
                )
            )
        },
        containerColor = userScreenBackgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Insertion d'une petite image (Logo)


            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "USER TASKS :",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = userScreenTextColor,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .drawBehind {
                        val startColor = userScreenSecondaryColor
                        val endColor = Color.Transparent
                        drawRect(
                            brush = Brush.horizontalGradient(
                                colors = listOf(startColor, endColor)
                            ),
                            topLeft = Offset(0f, size.height),
                            size = Size(size.width, 3.dp.toPx())
                        )
                    }
            )

            val buttonColors = ButtonDefaults.buttonColors(
                containerColor = userScreenPrimaryColor,
                contentColor = userScreenOnPrimaryColor
            )

            val tasks = listOf(
                "1. Data Science Project",
                "2. Mobile Computing Project",
                "3. IoT Semester Project",
                "4. Cloud Computing Project",
                "5. Semester Project"
            )

            tasks.forEach { taskTitle ->
                Button(
                    onClick = { onTaskSelected(taskTitle) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = buttonColors
                ) {
                    Text(taskTitle)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserTasksScreenPreview() {
    UserTasksScreen(onBack = {}, onTaskSelected = {}, onLogout = {})
}
