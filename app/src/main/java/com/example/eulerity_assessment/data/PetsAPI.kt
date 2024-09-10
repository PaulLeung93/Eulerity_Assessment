package com.example.eulerity_assessment.data

import retrofit2.http.GET

interface PetsAPI {
    @GET("/pets")
    suspend fun getPets(): List<Pet>
}