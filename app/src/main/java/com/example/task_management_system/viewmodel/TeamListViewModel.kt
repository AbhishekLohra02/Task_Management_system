package com.example.task_management_system.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task_management_system.data.Team
import com.example.task_management_system.data.User
import com.example.task_management_system.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TeamListViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        fetchTeams()
    }

    fun fetchTeams() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            val currentUserId = repository.getCurrentUserId()
            if (currentUserId != null) {
                val userResult = repository.getUserById(currentUserId)
                val currentUser = userResult.getOrNull()

                val result = repository.getTeams()
                val usersResult = repository.getUsers()
                
                if (usersResult.isSuccess) {
                    _users.value = usersResult.getOrDefault(emptyList())
                }

                if (result.isSuccess) {
                    val allTeams = result.getOrDefault(emptyList())
                    if (currentUser?.role == "SuperAdmin") {
                        _teams.value = allTeams
                    } else {
                        // Filter teams for Managers and Users:
                        // 1. Where user is a manager
                        // 2. Where user is in members list
                        // 3. Where user's teamId matches the team's id
                        _teams.value = allTeams.filter { team ->
                            team.managerId == currentUserId || 
                            team.members.contains(currentUserId) ||
                            team.id == currentUser?.teamId
                        }
                    }
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to fetch teams"
                }
            }
            
            _isLoading.value = false
        }
    }

    fun onTeamSelected(team: Team) {
        // Handle team selection logic here, e.g., saving selected team ID
        println("Team selected: ${team.name}")
    }
}
