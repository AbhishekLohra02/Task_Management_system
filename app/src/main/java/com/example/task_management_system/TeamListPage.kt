package com.example.task_management_system

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TeamListPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_team_list_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun teamlistscreenPreview() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Welcome To Team Selection page , User") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Text(text = "Team List")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Button(onClick = { "TeamA would be selected" }) {
                    Text(text = "Team Alpha")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Button(onClick = { "TeamB would be selected" }) {
                    Text(text = "Team Beta")
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Button(onClick = { "TeamC would be selected" }) {
                    Text(text = "Team Gamma")
                }
            }


        }

    }
    }