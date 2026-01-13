package com.example.task_management_system.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task_management_system.data.Team
import com.example.task_management_system.data.User
import com.example.task_management_system.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SuperAdminViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    // State for list of teams
    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams

    // State for operation results (Success/Failure messages)
    private val _operationStatus = MutableStateFlow<Result<Boolean>?>(null)
    val operationStatus: StateFlow<Result<Boolean>?> = _operationStatus

    fun fetchTeams() {
        viewModelScope.launch {
            val result = repository.getTeams()
            _teams.value = result.getOrDefault(teams.value)
            if(result.isSuccess) {
                _teams.value = result.getOrDefault(emptyList())
            }
        }
    }

    fun createManager(name: String, email: String, password: String) {
        viewModelScope.launch {
            val user = User(name = name, email = email, role = "Manager")
            val result = repository.registerUser(user, password)
            _operationStatus.value = result
        }
    }

    fun createTeam(teamName: String, managerId: String) {
        viewModelScope.launch {
            val result = repository.createTeam(teamName, managerId)
            _operationStatus.value = result
            if(result.isSuccess) {
                fetchTeams()
            }
        }
    }
    fun clearStatus() {
        _operationStatus.value = null
    }

    // Initial fetch
    init {

        fetchTeams()
    }
}
