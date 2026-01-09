package com.example.task_management_system.viewmodel

import androidx.lifecycle.ViewModel
import com.example.task_management_system.data.Team
import com.example.task_management_system.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TeamListViewModel : ViewModel() {

    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams.asStateFlow()

    // Example of using the User model to track the currently logged-in user
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        // Mock current user
        _currentUser.value = User(uid = "user123", name = "John Doe", role = "User", email = "john@example.com")

        _teams.value = listOf(
            Team(id = "1", name = "Team Alpha", description = "The core developers", managerId = "mgr1"),
            Team(id = "2", name = "Team Beta", description = "The design specialists", managerId = "mgr2"),
            Team(id = "3", name = "Team Gamma", description = "The testing unit", managerId = "mgr3"),
            Team(id = "4", name = "Team Delta", description = "Project management", managerId = "mgr4"),
            Team(id = "5", name = "Team Epsilon", description = "Maintenance and support", managerId = "mgr5")
        )
    }

    fun onTeamSelected(team: Team) {
        println("Team selected: ${team.name} by user: ${_currentUser.value?.name}") 
    }
}
