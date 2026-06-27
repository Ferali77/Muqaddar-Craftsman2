package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val notes: String = ""
)

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerId: Long? = null,
    val name: String,
    val type: String, // e.g., "شقة", "منزل", "فيلا", etc.
    val status: String = "قيد الانتظار", // "قيد الانتظار", "قيد العمل", "مكتمل"
    val createdAt: Long = System.currentTimeMillis(),
    val notes: String = "",
    val discount: Double = 0.0,
    val taxRate: Double = 15.0, // 15% VAT default
    val isMaterialCostIncluded: Boolean = true
)

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val name: String,
    val length: Double, // in meters
    val width: Double,  // in meters
    val height: Double, // in meters
    val hasCeiling: Boolean = true,
    val hasFlooring: Boolean = true,
    val paintCoats: Int = 2,
    val puttyCoats: Int = 2,
    val sandingRequired: Boolean = true,
    val wallPricePerM2: Double = 15.0, // unit price in local currency
    val ceilingPricePerM2: Double = 18.0,
    val flooringPricePerM2: Double = 25.0,
    val materialCostFactor: Double = 1.0
)

@Entity(tableName = "openings")
data class OpeningEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val roomId: Long,
    val type: String, // "باب داخلي", "نافذة", etc.
    val width: Double,
    val height: Double,
    val quantity: Int = 1
)

@Entity(tableName = "work_tasks")
data class WorkTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long,
    val roomId: Long? = null,
    val title: String,
    val unit: String, // "متر مربع", "متر طولي", "قطعة", "سعر ثابت"
    val unitPrice: Double,
    val quantity: Double
)

@Entity(tableName = "settings")
data class AppSetting(
    @PrimaryKey val key: String,
    val value: String
)
