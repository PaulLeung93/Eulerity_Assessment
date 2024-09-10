package com.example.eulerity_assessment.data

import com.example.eulerity_assessment.domain.model.Pet

class PetsRepository(private val apiService: PetsAPI) {
    suspend fun getPets(): List<Pet> {
        return apiService.getPets()
    }
}