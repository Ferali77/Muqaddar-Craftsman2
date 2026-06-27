package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CraftsmanDao {
    // --- Customers ---
    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomersFlow(): Flow<List<Customer>>

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: Long): Customer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer): Long

    @Update
    suspend fun updateCustomer(customer: Customer)

    @Query("DELETE FROM customers WHERE id = :id")
    suspend fun deleteCustomer(id: Long)

    // --- Projects ---
    @Query("SELECT * FROM projects ORDER BY createdAt DESC")
    fun getAllProjectsFlow(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getProjectById(id: Long): Project?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project): Long

    @Update
    suspend fun updateProject(project: Project)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProject(id: Long)

    // --- Rooms ---
    @Query("SELECT * FROM rooms WHERE projectId = :projectId ORDER BY id ASC")
    fun getRoomsForProjectFlow(projectId: Long): Flow<List<RoomEntity>>

    @Query("SELECT * FROM rooms WHERE projectId = :projectId ORDER BY id ASC")
    suspend fun getRoomsForProject(projectId: Long): List<RoomEntity>

    @Query("SELECT * FROM rooms WHERE id = :id")
    suspend fun getRoomById(id: Long): RoomEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoom(room: RoomEntity): Long

    @Update
    suspend fun updateRoom(room: RoomEntity)

    @Query("DELETE FROM rooms WHERE id = :id")
    suspend fun deleteRoom(id: Long)

    @Query("DELETE FROM rooms WHERE projectId = :projectId")
    suspend fun deleteRoomsForProject(projectId: Long)

    @Query("SELECT * FROM rooms")
    fun getAllRoomsFlow(): Flow<List<RoomEntity>>

    // --- Openings ---
    @Query("SELECT * FROM openings WHERE roomId = :roomId")
    fun getOpeningsForRoomFlow(roomId: Long): Flow<List<OpeningEntity>>

    @Query("SELECT * FROM openings WHERE roomId = :roomId")
    suspend fun getOpeningsForRoom(roomId: Long): List<OpeningEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOpening(opening: OpeningEntity): Long

    @Query("DELETE FROM openings WHERE id = :id")
    suspend fun deleteOpening(id: Long)

    @Query("DELETE FROM openings WHERE roomId = :roomId")
    suspend fun deleteOpeningsForRoom(roomId: Long)

    // --- Work Tasks ---
    @Query("SELECT * FROM work_tasks WHERE projectId = :projectId")
    fun getWorkTasksForProjectFlow(projectId: Long): Flow<List<WorkTaskEntity>>

    @Query("SELECT * FROM work_tasks WHERE projectId = :projectId")
    suspend fun getWorkTasksForProject(projectId: Long): List<WorkTaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkTask(task: WorkTaskEntity): Long

    @Query("DELETE FROM work_tasks WHERE id = :id")
    suspend fun deleteWorkTask(id: Long)

    // --- Settings ---
    @Query("SELECT * FROM settings WHERE `key` = :key")
    suspend fun getSetting(key: String): AppSetting?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSetting(setting: AppSetting)
}
