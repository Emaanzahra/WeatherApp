package com.example.weatherapp.Repository

import com.example.weatherapp.Server.ApiService

class WeatherRpository(private val apiService : ApiService) {
    fun getCurrentWeather (lat : Double, lon : Double, units : String) =
        apiService.getCurrentWeather(lat,lon,units,"0755941c39964c9234441a1f8db918e0" )

    fun getForecastWeather (lat : Double, lon : Double, units : String) =
        apiService.getForecastWeather(lat,lon,units,"0755941c39964c9234441a1f8db918e0" )

}