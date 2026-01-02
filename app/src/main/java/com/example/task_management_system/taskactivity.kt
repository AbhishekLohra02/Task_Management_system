@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.task_management_system

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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

// --- PALETTE DE COULEURS PROFESSIONNELLES ---
val primaryColor = Color(0xFF0D47A1)
val secondaryColor = Color(0xFF42A5F5)
val onPrimaryColor = Color.White
val backgroundColor = Color(0xFFF5F7FA)
val surfaceColor = Color.White
val textColorPrimary = Color(0xFF212121)
val textColorSecondary = Color(0xFF757575)
val accentColorError = Color(0xFFD32F2F)

data class TaskDetail(
    val title: String,
    val description: String,
    var status: String,
    var userComment: String
)

@Composable
fun TaskDetailScreen(
    onLogout: () -> Unit = {}
) {
    var task by remember {
        mutableStateOf(
            TaskDetail(
                title = "TASK: Mobile Project",
                description = "You have to submit the Mobile application Project before Christmas break",
                status = "In-Progress",
                userComment = ""
            )
        )
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "ProjeManager",
                        color = onPrimaryColor,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Log Out", color = onPrimaryColor)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = primaryColor
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = task.title,
                color = primaryColor,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .drawBehind {
                        val startColor = secondaryColor
                        val endColor = Color.Transparent
                        drawRect(
                            brush = Brush.horizontalGradient(colors = listOf(startColor, endColor)),
                            topLeft = Offset(0f, size.height),
                            size = Size(size.width, 3.dp.toPx())
                        )
                    }
            )

            Spacer(modifier = Modifier.height(30.dp))

            LabeledSection(
                label = "Tasks Description",
                content = {
                    Text(
                        text = task.description,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = textColorPrimary
                    )
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            StatusSelector(
                selectedStatus = task.status,
                onStatusChange = { newStatus ->
                    task = task.copy(status = newStatus)
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            LabeledSection(
                label = "User Comments",
                content = {
                    OutlinedTextField(
                        value = task.userComment,
                        onValueChange = { newComment ->
                            task = task.copy(userComment = newComment)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = primaryColor,
                            unfocusedBorderColor = textColorSecondary.copy(alpha = 0.5f),
                            cursorColor = primaryColor
                        ),
                        placeholder = { Text("Enter your comments...", color = textColorSecondary) }
                    )
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* TODO: Logique de sauvegarde */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = onPrimaryColor
                ),
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .fillMaxWidth()
            ) {
                Text("Save", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun LabeledSection(label: String, content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier.padding(top = 8.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            shape = MaterialTheme.shapes.medium,
            color = surfaceColor
        ) {
            content()
        }
        Text(
            text = " $label ",
            modifier = Modifier
                .padding(start = 12.dp)
                .background(backgroundColor),
            color = textColorSecondary,
            fontSize = 14.sp
        )
    }
}

@Composable
fun StatusSelector(selectedStatus: String, onStatusChange: (String) -> Unit) {
    val statusOptions = listOf("To Do", "In-Progress", "Done")

    Surface(
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, Color.LightGray),
        color = surfaceColor,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            statusOptions.forEach { status ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onStatusChange(status) }
                        .padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = (status == selectedStatus),
                        onClick = { onStatusChange(status) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = secondaryColor,
                            unselectedColor = textColorSecondary
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = status,
                        color = if (status == selectedStatus) primaryColor else textColorSecondary,
                        fontWeight = if (status == selectedStatus) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskDetailScreenPreview() {
    TaskDetailScreen()
}
