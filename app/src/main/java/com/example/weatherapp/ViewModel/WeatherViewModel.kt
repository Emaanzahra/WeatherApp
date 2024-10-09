package com.example.weatherapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.weatherapp.Repository.WeatherRpository
import com.example.weatherapp.Server.ApiClient
import com.example.weatherapp.Server.ApiService

class WeatherViewModel(val repository: WeatherRpository) : ViewModel() {
    constructor() : this(
        WeatherRpository(
            ApiClient()
                .getClient().create(ApiService::class.java)))
    fun loadCurrentWeather(lat: Double, lon: Double, units: String) =
        repository.getCurrentWeather(lat, lon, units)

    fun loadForecastWeather(lat: Double, lon: Double, units: String) =
        repository.getForecastWeather(lat, lon, units)
}


