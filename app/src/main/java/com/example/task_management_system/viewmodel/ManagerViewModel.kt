package com.example.task_management_system.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task_management_system.data.Task
import com.example.task_management_system.data.Team
import com.example.task_management_system.data.User
import com.example.task_management_system.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ManagerViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    private val _teamTasks = MutableStateFlow<List<Task>>(emptyList())
    val teamTasks: StateFlow<List<Task>> = _teamTasks.asStateFlow()

    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _operationStatus = MutableStateFlow<Result<Boolean>?>(null)
    val operationStatus: StateFlow<Result<Boolean>?> = _operationStatus.asStateFlow()

    init {
        fetchAllData()
    }

    fun fetchAllData() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Fetch Teams
            val teamsResult = repository.getTeams()
            if (teamsResult.isSuccess) {
                _teams.value = teamsResult.getOrDefault(emptyList())
            }

            // Fetch Users
            val usersResult = repository.getUsers()
            if (usersResult.isSuccess) {
                _users.value = usersResult.getOrDefault(emptyList())
            }

            // Fetch All Tasks (for progress tracking)
            val tasksResult = repository.getAllTasks()
            if (tasksResult.isSuccess) {
                _teamTasks.value = tasksResult.getOrDefault(emptyList())
            }
            
            _isLoading.value = false
        }
    }

    fun createUser(user: User, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.registerUser(user, password)
            _operationStatus.value = result
            if (result.isSuccess) fetchAllData()
            _isLoading.value = false
        }
    }

    fun createTeam(teamName: String, managerId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.createTeam(teamName, managerId)
            _operationStatus.value = result
            if (result.isSuccess) fetchAllData()
            _isLoading.value = false
        }
    }

    fun createTask(task: Task) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.createTask(task)
            _operationStatus.value = result
            if (result.isSuccess) fetchAllData()
            _isLoading.value = false
        }
    }

    fun clearStatus() {
        _operationStatus.value = null
    }
}
