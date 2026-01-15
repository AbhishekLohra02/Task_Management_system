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
            val authResult = auth.createUserWithEmailAndPassword(user.email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("Failed to create user")

            val newUser = user.copy(uid = uid)
            firestore.collection("users").document(uid).set(newUser).await()

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
            // Note: This only deletes from Firestore. Deleting from Auth requires Admin SDK or Cloud Functions
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
