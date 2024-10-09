package com.example.weatherapp.Repository

import com.example.weatherapp.Server.ApiService

class CityRepository (val api : ApiService) {

    fun getCity(city : String, limit : Int) = api.getCity(city, limit, Apikey = "0755941c39964c9234441a1f8db918e0" )
}