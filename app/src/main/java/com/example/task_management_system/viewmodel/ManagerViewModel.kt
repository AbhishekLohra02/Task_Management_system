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
            
            val currentUserId = repository.getCurrentUserId()
            if (currentUserId != null) {
                // Get current user profile to check role
                val currentUserResult = repository.getUserById(currentUserId)
                val currentUser = currentUserResult.getOrNull()

                if (currentUser != null) {
                    when (currentUser.role) {
                        "Manager" -> {
                            // 1. Get users created by the manager
                            val createdUsersResult = repository.getUsersByCreator(currentUserId)
                            val createdUsers = createdUsersResult.getOrDefault(emptyList())

                            // 2. Get teams managed by this manager
                            val teamsResult = repository.getTeamsByManager(currentUserId)
                            val managedTeams = teamsResult.getOrDefault(emptyList())
                            val managedTeamIds = managedTeams.map { it.id }.toSet()

                            // 3. Get all users to find team members (or use specific query if better)
                            val allUsersResult = repository.getUsers()
                            val teamUsers = if (allUsersResult.isSuccess) {
                                allUsersResult.getOrDefault(emptyList()).filter { it.teamId in managedTeamIds }
                            } else {
                                emptyList()
                            }

                            // Combined unique list: Self + Created + Team Members
                            _users.value = (listOf(currentUser) + createdUsers + teamUsers).distinctBy { it.uid }
                        }
                        "SuperAdmin" -> {
                            // SuperAdmin sees everyone
                            val allUsersResult = repository.getUsers()
                            if (allUsersResult.isSuccess) {
                                _users.value = allUsersResult.getOrDefault(emptyList())
                            }
                        }
                        else -> {
                            // Regular users might only see themselves (fallback)
                            _users.value = listOf(currentUser)
                        }
                    }
                }

                // Fetch Teams & Tasks for dashboard/progress
                if (currentUser?.role == "SuperAdmin") {
                    // SuperAdmin sees all teams and all tasks
                    val allTeamsResult = repository.getTeams()
                    if (allTeamsResult.isSuccess) {
                        val allTeams = allTeamsResult.getOrDefault(emptyList())
                        _teams.value = allTeams
                        
                        val allTasksResult = repository.getAllTasks()
                        if (allTasksResult.isSuccess) {
                            _teamTasks.value = allTasksResult.getOrDefault(emptyList())
                        }
                    }
                } else {
                    // Manager/Others see their managed teams
                    val teamsResult = repository.getTeamsByManager(currentUserId)
                    if (teamsResult.isSuccess) {
                        val managerTeams = teamsResult.getOrDefault(emptyList())
                        _teams.value = managerTeams
                        
                        // Fetch Tasks for these teams
                        val allTasksResult = repository.getAllTasks()
                        if (allTasksResult.isSuccess) {
                            val managerTeamIds = managerTeams.map { it.id }.toSet()
                            _teamTasks.value = allTasksResult.getOrDefault(emptyList()).filter { 
                                it.teamId in managerTeamIds 
                            }
                        }
                    }
                }
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
