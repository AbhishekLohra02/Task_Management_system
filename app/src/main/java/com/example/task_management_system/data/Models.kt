
package com.example.task_management_system.data



data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val role: String = "", // "SuperAdmin", "Manager", "User"
    val teamId: String? = null // Null for SuperAdmin, or if not assigned yet
)

data class Team(
    val id: String = "",
    val name: String = "",
    val managerId: String = "", // The manager leading this team
    val members: List<String> = emptyList() // List of User UIDs
)

data class Task(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val status: String = "To Do", // "To Do", "In Progress", "Done"
    val assignedTo: String = "", // UID of the user working on it
    val createdBy: String = "", // UID of the manager/admin who created it
    val userComment: String = ""
)
