package com.example.remote

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.Header
import com.example.model.Item
import com.example.model.Booking

interface RybApiService {
    @GET("items")
    suspend fun getItems(): List<Item>

    @POST("bookings")
    suspend fun createBooking(@Body booking: Booking): Booking

    @GET("bookings/{customerId}")
    suspend fun getBookings(@Path("customerId") customerId: String): List<Booking>
}

data class OpenRouterMessage(
    val role: String,
    val content: String
)

data class OpenRouterRequest(
    val model: String,
    val messages: List<OpenRouterMessage>
)

data class OpenRouterChoice(
    val message: OpenRouterMessage
)

data class OpenRouterResponse(
    val choices: List<OpenRouterChoice>
)

interface OpenRouterApiService {
    @POST("chat/completions")
    suspend fun chat(
        @Header("Authorization") authorization: String,
        @Header("HTTP-Referer") referer: String = "https://ai.studio/build",
        @Header("X-Title") title: String = "Rent Anything",
        @Body request: OpenRouterRequest
    ): OpenRouterResponse
}

object NetworkModule {
    private const val BASE_URL = "http://10.0.2.2:5000/api/"
    private const val OPENROUTER_BASE_URL = "https://openrouter.ai/api/v1/"

    private val moshi = com.squareup.moshi.Moshi.Builder()
        .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
    
    val api: RybApiService by lazy {
        retrofit.create(RybApiService::class.java)
    }

    val openRouterRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(OPENROUTER_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    val openRouterApi: OpenRouterApiService by lazy {
        openRouterRetrofit.create(OpenRouterApiService::class.java)
    }
}
