package com.example.weatherapp.Server

import com.example.weatherapp.Model.CityResponseApi
import retrofit2.Call
import com.example.weatherapp.Model.CurrentResponseApi
import com.example.weatherapp.Model.ForecastResponseApi
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") lat : Double,
        @Query("lon") lon : Double,
        @Query("units") units : String,
        @Query("appid") Apikey : String
        ) : Call<CurrentResponseApi>

    @GET("data/2.5/weather")
    fun getForecastWeather(
        @Query("lat") lat : Double,
        @Query("lon") lon : Double,
        @Query("units") units : String,
        @Query("appid") Apikey : String
    ) : Call<ForecastResponseApi>

    @GET("geo/1.0/direct")
    fun getCity(
        @Query("q") q : String,
        @Query("limit") limit : Int,
        @Query("appid") Apikey: String
    ) : Call<CityResponseApi>
}