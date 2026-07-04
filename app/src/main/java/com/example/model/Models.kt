package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val category: String, // "Appliances", "Business", "Vehicles", "Education"
    val specs: String, // generic specs instead of cc or topSpeed
    val pricePerHour: Double,
    val imageUrl: String,
    val rating: Double,
    val location: String,
    val isAvailable: Boolean = true,
    val ownerId: String = "admin"
)

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemId: Int,
    val customerName: String,
    val status: String, // "Pending", "Active", "Completed", "Cancelled"
    val startTime: Long,
    val endTime: Long,
    val totalAmount: Double
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = "admin",
    val name: String,
    val email: String,
    val phone: String,
    val role: String = "Customer", // Customer, Owner, Admin
    val walletBalance: Double = 0.0,
    val isVerified: Boolean = false
)

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val itemId: Int,
    val userId: String,
    val userName: String,
    val rating: Double,
    val comment: String,
    val timestamp: Long = System.currentTimeMillis()
)

