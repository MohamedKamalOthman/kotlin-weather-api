package com.example.weather

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Interface for product api
 * @see retrofit2.http.GET
 * @see Weather
 * @see WeatherApi
 * @see Query
 * @see GET
 */
interface ProductApiService {
    @GET("v1/forecast.json")
    suspend fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("aqi") includeAqi: String,
        @Query("days") forecastDays: Int
    ): Weather
}

/**
 * Object for product api
 * @see retrofit2.Retrofit
 * @see Moshi
 */
object WeatherApi {
    private val BASE_URL =
        "https://api.weatherapi.com/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    val retrofitService: ProductApiService by lazy {
        retrofit.create(ProductApiService::class.java)
    }
}
