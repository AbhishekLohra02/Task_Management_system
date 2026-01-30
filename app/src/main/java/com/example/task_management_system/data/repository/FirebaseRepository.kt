package com.example.task_management_system.data.repository

import com.example.task_management_system.data.Task
import com.example.task_management_system.data.Team
import com.example.task_management_system.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Secondary Firebase Auth instance for creating users without logging out the manager
    private var secondaryAuth: FirebaseAuth? = null

    private fun getSecondaryAuth(): FirebaseAuth {
        if (secondaryAuth == null) {
            val context = com.google.firebase.FirebaseApp.getInstance().applicationContext
            val options = com.google.firebase.FirebaseApp.getInstance().options
            val secondaryApp = try {
                com.google.firebase.FirebaseApp.getInstance("Secondary")
            } catch (e: Exception) {
                com.google.firebase.FirebaseApp.initializeApp(context, options, "Secondary")
            }
            secondaryAuth = FirebaseAuth.getInstance(secondaryApp)
        }
        return secondaryAuth!!
    }

    // --- Authentication ---

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("User ID not found")

            val documentSnapshot = firestore.collection("users").document(uid).get().await()
            val user = documentSnapshot.toObject(User::class.java)

            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    // --- User Management ---

    suspend fun registerUser(user: User, password: String): Result<Boolean> {
        return try {
            val managerId = auth.currentUser?.uid ?: ""
            val sAuth = getSecondaryAuth()
            
            val authResult = sAuth.createUserWithEmailAndPassword(user.email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("Failed to create user")

            val newUser = user.copy(uid = uid, createdBy = managerId)
            firestore.collection("users").document(uid).set(newUser).await()
            
            // Sign out from the secondary instance to keep it clean
            sAuth.signOut()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsers(): Result<List<User>> {
        return try {
            val snapshot = firestore.collection("users").get().await()
            val users = snapshot.toObjects(User::class.java)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsersByRole(role: String): Result<List<User>> {
        return try {
            val snapshot = firestore.collection("users")
                .whereEqualTo("role", role)
                .get()
                .await()
            val users = snapshot.toObjects(User::class.java)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser(uid: String): Result<Boolean> {
        return try {
            firestore.collection("users").document(uid).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsersByTeam(teamId: String): Result<List<User>> {
        return try {
            val snapshot = firestore.collection("users")
                .whereEqualTo("teamId", teamId)
                .get()
                .await()
            val users = snapshot.toObjects(User::class.java)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(uid: String): Result<User?> {
        return try {
            val snapshot = firestore.collection("users").document(uid).get().await()
            val user = snapshot.toObject(User::class.java)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUsersByCreator(creatorId: String): Result<List<User>> {
        return try {
            val snapshot = firestore.collection("users")
                .whereEqualTo("createdBy", creatorId)
                .get()
                .await()
            val users = snapshot.toObjects(User::class.java)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Team Management ---

    suspend fun createTeam(teamName: String, managerId: String): Result<Boolean> {
        return try {
            val teamId = firestore.collection("teams").document().id
            val team = Team(id = teamId, name = teamName, managerId = managerId)
            firestore.collection("teams").document(teamId).set(team).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTeams(): Result<List<Team>> {
        return try {
            val snapshot = firestore.collection("teams").get().await()
            val teams = snapshot.toObjects(Team::class.java)
            Result.success(teams)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTeamsByManager(managerId: String): Result<List<Team>> {
        return try {
            val snapshot = firestore.collection("teams")
                .whereEqualTo("managerId", managerId)
                .get()
                .await()
            val teams = snapshot.toObjects(Team::class.java)
            Result.success(teams)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // --- Task Management ---

    suspend fun createTask(task: Task): Result<Boolean> {
        return try {
            val taskId = firestore.collection("tasks").document().id
            val newTask = task.copy(id = taskId)
            firestore.collection("tasks").document(taskId).set(newTask).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllTasks(): Result<List<Task>> {
        return try {
            val snapshot = firestore.collection("tasks").get().await()
            val tasks = snapshot.toObjects(Task::class.java)
            Result.success(tasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTaskById(taskId: String): Result<Task?> {
        return try {
            val snapshot = firestore.collection("tasks").document(taskId).get().await()
            val task = snapshot.toObject(Task::class.java)
            Result.success(task)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTasksForUser(userId: String): Result<List<Task>> {
        return try {
            val snapshot = firestore.collection("tasks")
                .whereEqualTo("assignedTo", userId)
                .get()
                .await()
            val tasks = snapshot.toObjects(Task::class.java)
            Result.success(tasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTasksByTeamForUser(userId: String, teamId: String): Result<List<Task>> {
        return try {
            val snapshot = firestore.collection("tasks")
                .whereEqualTo("assignedTo", userId)
                .whereEqualTo("teamId", teamId)
                .get()
                .await()
            val tasks = snapshot.toObjects(Task::class.java)
            Result.success(tasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTask(task: Task): Result<Boolean> {
        return try {
            firestore.collection("tasks").document(task.id).set(task).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTaskStatus(taskId: String, newStatus: String): Result<Boolean> {
        return try {
            firestore.collection("tasks").document(taskId)
                .update("status", newStatus)
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
