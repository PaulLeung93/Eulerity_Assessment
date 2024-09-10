package com.example.eulerity_assessment.data

class PetsRepository(private val apiService: PetsAPI) {
    suspend fun getPets(): List<Pet> {
        return apiService.getPets()
    }
}