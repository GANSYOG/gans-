package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.ItemDao
import com.example.data.BookingDao
import com.example.data.ReviewDao
import com.example.data.UserDao
import com.example.data.RybRepository
import com.example.model.Item
import com.example.model.Review
import com.example.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppContainer(private val context: Context) {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "ryb_database")
            .fallbackToDestructiveMigration(true)
            .build()
            .also { db ->
                CoroutineScope(Dispatchers.IO).launch {
                    val itemDao = db.itemDao()
                    if (itemDao.getItemsCount() == 0) {
                        itemDao.insertItems(getDummyItems())
                    }
                    val userDao = db.userDao()
                    userDao.insertUser(User(id = "current_user", name = "Jane Does", email = "jane@example.com", phone = "+1234567890", role = "Customer", walletBalance = 500.0, isVerified = true))
                    
                    val reviewDao = db.reviewDao()
                    // Add some dummy reviews
                    reviewDao.insertReview(Review(itemId = 1, userId = "user1", userName = "Alex", rating = 4.5, comment = "Worked great!"))
                    reviewDao.insertReview(Review(itemId = 1, userId = "user2", userName = "Sam", rating = 5.0, comment = "In excellent condition."))
                }
            }
    }
    
    val itemDao: ItemDao by lazy { database.itemDao() }
    val bookingDao: BookingDao by lazy { database.bookingDao() }
    val userDao: UserDao by lazy { database.userDao() }
    val reviewDao: ReviewDao by lazy { database.reviewDao() }

    val repository: RybRepository by lazy {
        RybRepository(itemDao, userDao, bookingDao, reviewDao)
    }
    
    private fun getDummyItems() = listOf(
        Item(name = "Royal Enfield Classic 350", description = "Cruise bike for long rides", category = "Vehicles", specs = "350cc, Gear", pricePerHour = 50.0, imageUrl = "https://images.unsplash.com/photo-1558981403-c5f9899a28bc?q=80&w=2070&auto=format&fit=crop", rating = 4.8, location = "Downtown Hub"),
        Item(name = "Sony A7III Camera", description = "Professional mirrorless camera with 28-70mm lens", category = "Electronics", specs = "24.2MP Full-Frame, 4K Video", pricePerHour = 30.0, imageUrl = "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?q=80&w=2070&auto=format&fit=crop", rating = 4.9, location = "City Center"),
        Item(name = "Makita Power Drill", description = "Cordless driller and driver kit", category = "Tools", specs = "18V LXT Lithium-Ion", pricePerHour = 10.0, imageUrl = "https://images.unsplash.com/photo-1504148455328-c376907d081c?q=80&w=2070&auto=format&fit=crop", rating = 4.5, location = "Hardware Store"),
        Item(name = "Projector & Screen", description = "1080p HD Projector for events", category = "Business", specs = "1080p Native, 300 inch support", pricePerHour = 20.0, imageUrl = "https://images.unsplash.com/photo-1563206767-5b18f218e8de?q=80&w=2069&auto=format&fit=crop", rating = 4.7, location = "Tech Park"),
        Item(name = "Camping Tent", description = "4-person weather-resistant tent", category = "Outdoors", specs = "4 Person, Waterproof", pricePerHour = 15.0, imageUrl = "https://images.unsplash.com/photo-1504280327387-9bb3a6efae9d?q=80&w=2070&auto=format&fit=crop", rating = 4.6, location = "South Campus"),
        Item(name = "Calculus Textbook", description = "University level math book", category = "Education", specs = "8th Edition", pricePerHour = 2.0, imageUrl = "https://images.unsplash.com/photo-1544947950-fa07a98d237f?q=80&w=2070&auto=format&fit=crop", rating = 4.8, location = "North Station")
    )
}

