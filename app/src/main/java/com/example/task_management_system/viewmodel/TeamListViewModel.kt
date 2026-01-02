package com.example.task_management_system.viewmodel

import androidx.lifecycle.ViewModel
import com.example.task_management_system.data.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TeamListViewModel : ViewModel() {

    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams.asStateFlow()

    init {
        // Initialize with mocked data matching the user's current button names
        // In the future, this would fetch from Firebase
        _teams.value = listOf(
            Team(id = "1", name = "Team Alpha", description = "Alpha Team Description"),
            Team(id = "2", name = "Team Beta", description = "Beta Team Description"),
            Team(id = "3", name = "Team Gamma", description = "Gamma Team Description"),
            // Added a few more to demonstrate scrolling
            Team(id = "4", name = "Team Delta", description = "Delta Team Description"),
            Team(id = "5", name = "Team Epsilon", description = "Epsilon Team Description")
        )
    }

    // Placeholder for future logic when a team is selected
    fun onTeamSelected(team: Team) {
        println("Team selected: ${team.name}") 
    }
}
