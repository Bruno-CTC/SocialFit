package com.example.socialfit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {
    @POST("/usuarios")
    fun registerUser(@Body user: UserData): Call<String>
}