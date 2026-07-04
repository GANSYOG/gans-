package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.model.Item
import com.example.model.Booking
import com.example.model.Review
import com.example.model.User

@Database(entities = [Item::class, Booking::class, User::class, Review::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun bookingDao(): BookingDao
    abstract fun userDao(): UserDao
    abstract fun reviewDao(): ReviewDao
}
