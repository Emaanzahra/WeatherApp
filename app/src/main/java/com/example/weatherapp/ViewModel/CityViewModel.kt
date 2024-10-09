package com.example.weatherapp.ViewModel

import androidx.lifecycle.ViewModel
import com.example.weatherapp.Model.CurrentResponseApi
import com.example.weatherapp.Repository.CityRepository
import com.example.weatherapp.Server.ApiClient
import com.example.weatherapp.Server.ApiService
import retrofit2.Call

class CityViewModel(val repository: CityRepository) : ViewModel() {
    constructor() : this(
        CityRepository(
            ApiClient().getClient().create(ApiService::class.java)
        )
    )

    // Correct the method to pass the limit correctly
    fun loadCity(city: String, limit: Int) =  repository.getCity(city, limit)

}
