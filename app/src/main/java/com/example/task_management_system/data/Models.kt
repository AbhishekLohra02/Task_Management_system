
package com.example.task_management_system.data

data class User(
    val id: Int = 0,
    val uid: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val address: String = "",
    val role: String = "", // "SuperAdmin", "Manager", "User"
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
    val status: String = "To Do", // "To Do", "In Progress", "Done"
    val assignedTo: String = "", // UID of the user
    val createdBy: String = "", // UID of the manager
    val userComment: String = ""
)
