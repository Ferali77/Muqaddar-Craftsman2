package com.example.data

import kotlinx.coroutines.flow.Flow

class CraftsmanRepository(private val dao: CraftsmanDao) {
    // Customers
    val allCustomers: Flow<List<Customer>> = dao.getAllCustomersFlow()
    suspend fun getCustomerById(id: Long): Customer? = dao.getCustomerById(id)
    suspend fun insertCustomer(customer: Customer): Long = dao.insertCustomer(customer)
    suspend fun updateCustomer(customer: Customer) = dao.updateCustomer(customer)
    suspend fun deleteCustomer(id: Long) = dao.deleteCustomer(id)

    // Projects
    val allProjects: Flow<List<Project>> = dao.getAllProjectsFlow()
    suspend fun getProjectById(id: Long): Project? = dao.getProjectById(id)
    suspend fun insertProject(project: Project): Long = dao.insertProject(project)
    suspend fun updateProject(project: Project) = dao.updateProject(project)
    suspend fun deleteProject(id: Long) {
        val rooms = dao.getRoomsForProject(id)
        for (room in rooms) {
            dao.deleteOpeningsForRoom(room.id)
        }
        dao.deleteRoomsForProject(id)
        dao.deleteProject(id)
    }

    // Rooms
    val allRooms: Flow<List<RoomEntity>> = dao.getAllRoomsFlow()
    fun getRoomsForProjectFlow(projectId: Long): Flow<List<RoomEntity>> = dao.getRoomsForProjectFlow(projectId)
    suspend fun getRoomsForProject(projectId: Long): List<RoomEntity> = dao.getRoomsForProject(projectId)
    suspend fun getRoomById(id: Long): RoomEntity? = dao.getRoomById(id)
    suspend fun insertRoom(room: RoomEntity): Long = dao.insertRoom(room)
    suspend fun updateRoom(room: RoomEntity) = dao.updateRoom(room)
    suspend fun deleteRoom(id: Long) {
        dao.deleteOpeningsForRoom(id)
        dao.deleteRoom(id)
    }

    // Openings
    fun getOpeningsForRoomFlow(roomId: Long): Flow<List<OpeningEntity>> = dao.getOpeningsForRoomFlow(roomId)
    suspend fun getOpeningsForRoom(roomId: Long): List<OpeningEntity> = dao.getOpeningsForRoom(roomId)
    suspend fun insertOpening(opening: OpeningEntity): Long = dao.insertOpening(opening)
    suspend fun deleteOpening(id: Long) = dao.deleteOpening(id)

    // WorkTasks
    fun getWorkTasksForProjectFlow(projectId: Long): Flow<List<WorkTaskEntity>> = dao.getWorkTasksForProjectFlow(projectId)
    suspend fun getWorkTasksForProject(projectId: Long): List<WorkTaskEntity> = dao.getWorkTasksForProject(projectId)
    suspend fun insertWorkTask(task: WorkTaskEntity): Long = dao.insertWorkTask(task)
    suspend fun deleteWorkTask(id: Long) = dao.deleteWorkTask(id)

    // Settings
    suspend fun getSetting(key: String, default: String): String {
        return dao.getSetting(key)?.value ?: default
    }
    suspend fun saveSetting(key: String, value: String) {
        dao.saveSetting(AppSetting(key, value))
    }
}
