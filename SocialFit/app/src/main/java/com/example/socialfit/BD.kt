package com.example.socialfit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// bd class that will connect to the api using retrofit
class BD {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://socialfitapi.herokuapp.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun getApi(): APIService {
        return retrofit.create(APIService::class.java)
    }
}