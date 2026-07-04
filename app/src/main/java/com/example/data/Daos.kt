package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.Item
import com.example.model.Booking
import com.example.model.Review
import com.example.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT COUNT(*) FROM items")
    suspend fun getItemsCount(): Int

    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE id = :id LIMIT 1")
    fun getItemById(id: Int): Flow<Item?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<Item>)

    @Update
    suspend fun updateItem(item: Item)
}

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY startTime DESC")
    fun getAllBookings(): Flow<List<Booking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)
}

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserById(id: String): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE itemId = :itemId ORDER BY timestamp DESC")
    fun getReviewsForItem(itemId: Int): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review)
}
