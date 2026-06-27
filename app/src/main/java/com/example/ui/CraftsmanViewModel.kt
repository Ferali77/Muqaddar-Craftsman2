package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CraftsmanViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    val repository = CraftsmanRepository(db.craftsmanDao())

    val allCustomers = repository.allCustomers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allProjects = repository.allProjects.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allRooms = repository.allRooms.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedProjectId = MutableStateFlow<Long?>(null)
    val selectedProjectId = _selectedProjectId.asStateFlow()

    val currentProject = _selectedProjectId.flatMapLatest { id ->
        if (id == null) flowOf<Project?>(null)
        else flow { emit(repository.getProjectById(id)) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val roomsForSelectedProject = _selectedProjectId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList())
        else repository.getRoomsForProjectFlow(id)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val tasksForSelectedProject = _selectedProjectId.flatMapLatest { id ->
        if (id == null) flowOf(emptyList())
        else repository.getWorkTasksForProjectFlow(id)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // A state flow of all openings in a project mapped by room ID
    val openingsMapForProject = roomsForSelectedProject.flatMapLatest { rooms ->
        if (rooms.isEmpty()) {
            flowOf(emptyMap<Long, List<OpeningEntity>>())
        } else {
            // Retrieve openings for all rooms and combine
            val flows = rooms.map { room ->
                repository.getOpeningsForRoomFlow(room.id).map { room.id to it }
            }
            combine(flows) { pairs ->
                pairs.toMap()
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyMap()
    )

    fun selectProject(projectId: Long?) {
        _selectedProjectId.value = projectId
    }

    // --- Customer Actions ---
    fun addCustomer(name: String, phone: String, email: String, address: String, notes: String, onComplete: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repository.insertCustomer(Customer(name = name, phone = phone, email = email, address = address, notes = notes))
            onComplete(id)
        }
    }

    fun deleteCustomer(id: Long) {
        viewModelScope.launch {
            repository.deleteCustomer(id)
        }
    }

    // --- Project Actions ---
    fun addProject(name: String, type: String, customerId: Long?, notes: String, discount: Double, taxRate: Double, onComplete: (Long) -> Unit = {}) {
        viewModelScope.launch {
            val id = repository.insertProject(Project(
                name = name,
                type = type,
                customerId = customerId,
                notes = notes,
                discount = discount,
                taxRate = taxRate
            ))
            onComplete(id)
        }
    }

    fun updateProjectStatus(projectId: Long, status: String) {
        viewModelScope.launch {
            val p = repository.getProjectById(projectId)
            if (p != null) {
                repository.updateProject(p.copy(status = status))
            }
        }
    }

    fun updateProjectSettings(projectId: Long, discount: Double, taxRate: Double) {
        viewModelScope.launch {
            val p = repository.getProjectById(projectId)
            if (p != null) {
                repository.updateProject(p.copy(discount = discount, taxRate = taxRate))
            }
        }
    }

    fun deleteProject(id: Long) {
        viewModelScope.launch {
            repository.deleteProject(id)
            if (_selectedProjectId.value == id) {
                _selectedProjectId.value = null
            }
        }
    }

    // --- Room Actions ---
    fun addRoom(
        projectId: Long,
        name: String,
        length: Double,
        width: Double,
        height: Double,
        hasCeiling: Boolean,
        hasFlooring: Boolean,
        paintCoats: Int,
        puttyCoats: Int,
        sandingRequired: Boolean,
        wallPrice: Double,
        ceilingPrice: Double,
        flooringPrice: Double,
        materialFactor: Double,
        onComplete: (Long) -> Unit = {}
    ) {
        viewModelScope.launch {
            val id = repository.insertRoom(RoomEntity(
                projectId = projectId,
                name = name,
                length = length,
                width = width,
                height = height,
                hasCeiling = hasCeiling,
                hasFlooring = hasFlooring,
                paintCoats = paintCoats,
                puttyCoats = puttyCoats,
                sandingRequired = sandingRequired,
                wallPricePerM2 = wallPrice,
                ceilingPricePerM2 = ceilingPrice,
                flooringPricePerM2 = flooringPrice,
                materialCostFactor = materialFactor
            ))
            onComplete(id)
        }
    }

    fun deleteRoom(roomId: Long) {
        viewModelScope.launch {
            repository.deleteRoom(roomId)
        }
    }

    // --- Opening Actions ---
    fun addOpening(roomId: Long, type: String, width: Double, height: Double, quantity: Int) {
        viewModelScope.launch {
            repository.insertOpening(OpeningEntity(
                roomId = roomId,
                type = type,
                width = width,
                height = height,
                quantity = quantity
            ))
        }
    }

    fun deleteOpening(openingId: Long) {
        viewModelScope.launch {
            repository.deleteOpening(openingId)
        }
    }

    // --- Custom Tasks Action ---
    fun addWorkTask(projectId: Long, title: String, unit: String, unitPrice: Double, quantity: Double) {
        viewModelScope.launch {
            repository.insertWorkTask(WorkTaskEntity(
                projectId = projectId,
                title = title,
                unit = unit,
                unitPrice = unitPrice,
                quantity = quantity
            ))
        }
    }

    fun deleteWorkTask(taskId: Long) {
        viewModelScope.launch {
            repository.deleteWorkTask(taskId)
        }
    }

    // --- PDF Generation ---
    fun exportAndSharePdf(context: android.content.Context, project: Project) {
        viewModelScope.launch {
            val customer = project.customerId?.let { repository.getCustomerById(it) }
            val rooms = repository.getRoomsForProject(project.id)
            val openings = mutableMapOf<Long, List<OpeningEntity>>()
            for (room in rooms) {
                openings[room.id] = repository.getOpeningsForRoom(room.id)
            }
            val tasks = repository.getWorkTasksForProject(project.id)
            PdfExporter.generateAndShareProjectPdf(context, project, customer, rooms, openings, tasks)
        }
    }
}
