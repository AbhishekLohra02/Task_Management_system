package com.example.task_management_system.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.task_management_system.data.Task
import com.example.task_management_system.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Fetches ALL tasks assigned to a user (used in TaskListScreen)
    fun fetchTasksForUser(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.getTasksForUser(userId)
            if (result.isSuccess) {
                _tasks.value = result.getOrDefault(emptyList())
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to fetch tasks"
            }
            _isLoading.value = false
        }
    }

    // Fetches tasks filtered by BOTH user and team (used in UserTasksScreen)
    fun fetchTasksByTeamForUser(userId: String, teamId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.getTasksByTeamForUser(userId, teamId)
            if (result.isSuccess) {
                _tasks.value = result.getOrDefault(emptyList())
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to fetch tasks"
            }
            _isLoading.value = false
        }
    }

    fun fetchTaskById(taskId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            val result = repository.getTaskById(taskId)
            if (result.isSuccess) {
                _selectedTask.value = result.getOrNull()
            } else {
                _errorMessage.value = "Task not found"
            }
            _isLoading.value = false
        }
    }

    fun saveTask(task: Task) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.updateTask(task)
            if (result.isSuccess) {
                _selectedTask.value = task
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to save task"
            }
            _isLoading.value = false
        }
    }

    fun updateTaskStatus(taskId: String, newStatus: String) {
        viewModelScope.launch {
            val result = repository.updateTaskStatus(taskId, newStatus)
            if (result.isSuccess) {
                val currentTasks = _tasks.value.map {
                    if (it.id == taskId) it.copy(status = newStatus) else it
                }
                _tasks.value = currentTasks
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to update task status"
            }
        }
    }
}
