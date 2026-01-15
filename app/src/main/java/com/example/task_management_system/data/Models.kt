package com.example.task_management_system.data

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val surname: String = "",
    val address: String = "",
    val role: String = "",
    val teamId: String? = null
)

data class Team(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val managerId: String = "",
    val members: List<String> = emptyList()
)

data class Task(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val status: String = "To Do",
    val assignedTo: String = "",
    val createdBy: String = "",
    val userComment: String = ""
)