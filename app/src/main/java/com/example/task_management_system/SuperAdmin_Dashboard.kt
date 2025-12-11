package com.example.task_management_system

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp



data class TeamData(val name: String, val progress: String)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperAdminScreen() {


    var screenState by remember { mutableStateOf("dashboard") }
    var selectedTeam by remember { mutableStateOf<TeamData?>(null) }

    val teamList = listOf(
        TeamData("Team Alpha", "UI Design Completed"),
        TeamData("Team Beta", "API Development In Progress"),
        TeamData("Team Gamma", "Testing Started")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Welcome Super Admin") }
            )
        }
    ) { padding ->

        when (screenState) {


            "dashboard" -> {
                SuperAdminDashboardContentPreview(
                    modifier = Modifier.padding(padding),
                    onCreateManager = {},
                    onCreateTeam = {},
                    onViewTeams = { screenState = "teams" }
                )
            }


            "teams" -> {
                TeamListScreen(
                    teams = teamList,
                    modifier = Modifier.padding(padding),
                    onTeamClick = { team ->
                        selectedTeam = team
                        screenState = "teamDetail"
                    }
                )
            }


            "teamDetail" -> {
                TeamDetailScreen(
                    team = selectedTeam!!,
                    modifier = Modifier.padding(padding),
                    onBack = { screenState = "teams" }
                )
            }
        }
    }
}

@Preview
@Composable
fun SuperAdminDashboardContentPreview(
    modifier: Modifier,
    onCreateManager: () -> Unit,
    onCreateTeam: () -> Unit,
    onViewTeams: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            onClick = onCreateManager,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Manager")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCreateTeam,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Team")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onViewTeams,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View All Teams")
        }
    }
}


@Composable
fun TeamListScreen(
    teams: List<TeamData>,
    modifier: Modifier,
    onTeamClick: (TeamData) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        items(teams) { team ->
            Button(
                onClick = { onTeamClick(team) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(team.name)
            }
        }
    }
}


@Composable
fun TeamDetailScreen(
    team: TeamData,
    modifier: Modifier,
    onBack: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(team.name, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Text("Progress: ${team.progress}")

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = onBack) {
            Text("Back to Teams")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SuperAdminPreview() {
    SuperAdminScreen()
}
