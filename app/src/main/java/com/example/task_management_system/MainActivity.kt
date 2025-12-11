package com.example.task_management_system

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
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
import com.example.task_management_system.ui.theme.ProjecManagTheme

// --- DÉFINITION D'UNE PALETTE PROFESSIONNELLE POUR LE SPLASH SCREEN ---
val splashBackgroundColor = Color(0xFF0D47A1) // Bleu foncé, sobre et corporate
val splashTextColor = Color.White              // Texte blanc pour un contraste élevé
val splashAccentColor = Color(0xFF42A5F5)        // Bleu plus clair pour l'accent du soulignement

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjecManagTheme {
                // On affiche directement le SplashScreen
                SplashScreen()
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            // MODIFIÉ : Utilisation de la nouvelle couleur de fond
            .background(splashBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        // MODIFIÉ : Les couleurs du dégradé utilisent maintenant la palette professionnelle
        val startColor = splashAccentColor
        val endColor = Color.Transparent

        Text(
            text = "ProjeManager",
            // MODIFIÉ : Utilisation de la nouvelle couleur de texte
            color = splashTextColor,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.drawBehind {
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(startColor, endColor)
                    ),
                    topLeft = Offset(0f, size.height - 4.dp.toPx()),
                    size = Size(size.width, 6.dp.toPx())
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    ProjecManagTheme {
        SplashScreen()
    }
}
