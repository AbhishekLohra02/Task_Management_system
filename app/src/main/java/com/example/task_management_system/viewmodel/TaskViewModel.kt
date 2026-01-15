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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

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

    fun updateTaskStatus(taskId: String, newStatus: String) {
        viewModelScope.launch {
            val result = repository.updateTaskStatus(taskId, newStatus)
            if (result.isSuccess) {
                // Optionally refresh the list or update local state
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
