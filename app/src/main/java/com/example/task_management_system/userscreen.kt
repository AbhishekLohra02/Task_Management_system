package com.example.task_management_system

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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

// --- PALETTE DE COULEURS RENOMMÉE POUR CET ÉCRAN ---
val userScreenPrimaryColor = Color(0xFF0D47A1)      // Bleu foncé
val userScreenSecondaryColor = Color(0xFF42A5F5)    // Bleu clair
val userScreenBackgroundColor = Color(0xFFF5F5F5)   // Fond gris clair
val userScreenOnPrimaryColor = Color.White        // Texte sur fond bleu
val userScreenTextColor = Color(0xFF212121)         // Texte principal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "ProjeManage",
                        color = userScreenOnPrimaryColor, // MODIFIÉ
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    TextButton(onClick = { /* TODO: Logique de déconnexion */ }) {
                        Text("LogOut", color = userScreenOnPrimaryColor) // MODIFIÉ
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = userScreenPrimaryColor // MODIFIÉ
                )
            )
        },
        containerColor = userScreenBackgroundColor // MODIFIÉ
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "USER TASKS :",
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = userScreenTextColor, // MODIFIÉ
                modifier = Modifier
                    .padding(bottom = 38.dp)
                    .drawBehind {
                        val startColor = userScreenSecondaryColor // MODIFIÉ
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
                containerColor = userScreenPrimaryColor, // MODIFIÉ
                contentColor = userScreenOnPrimaryColor  // MODIFIÉ
            )

            Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth(), colors = buttonColors) {
                Text("1. Data Science Project")
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth(), colors = buttonColors) {
                Text("2. Mobile Computing Project")
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth(), colors = buttonColors) {
                Text("3. IoT Semester Project")
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth(), colors = buttonColors) {
                Text("4. Cloud Computing Project")
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth(), colors = buttonColors) {
                Text("5. Semester Project")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
