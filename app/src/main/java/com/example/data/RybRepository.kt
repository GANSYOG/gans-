package com.example.data

import com.example.model.Booking
import com.example.remote.NetworkModule
import com.example.remote.RybApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class RybRepository(
    private val itemDao: ItemDao,
    private val userDao: UserDao,
    private val bookingDao: BookingDao,
    private val reviewDao: ReviewDao,
    private val api: RybApiService = NetworkModule.api
) {

    suspend fun syncItems() {
        try {
            val remoteItems = api.getItems()
            if (remoteItems.isNotEmpty()) {
                itemDao.insertItems(remoteItems)
            }
        } catch (e: Exception) {
            // Backend unreachable, fallback to DB
        }
    }

    suspend fun createBooking(booking: Booking, onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            // First save locally to ensure offline capability
            val localId = bookingDao.insertBooking(booking)
            
            try {
                // Try to sync with server
                val remoteBooking = api.createBooking(booking)
                onSuccess()
            } catch (e: Exception) {
                // Ignore network error for Offline First
                onSuccess()
            }
        } catch (e: Exception) {
            onError("Failed to create booking: ${e.message}")
        }
    }

    suspend fun syncBookings(customerId: String) {
        try {
            val remoteBookings = api.getBookings(customerId)
            if (remoteBookings.isNotEmpty()) {
                // For simplicity, just insert them all. In prod, you'd handle updates/inserts properly.
                remoteBookings.forEach { bookingDao.insertBooking(it) }
            }
        } catch (e: Exception) {
            // Backend unreachable, keep using local DB
        }
    }
}
