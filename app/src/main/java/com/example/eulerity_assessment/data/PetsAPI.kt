package com.example.eulerity_assessment.data

import com.example.eulerity_assessment.domain.model.Pet
import retrofit2.http.GET

interface PetsAPI {
    @GET("/pets")
    suspend fun getPets(): List<Pet>
}