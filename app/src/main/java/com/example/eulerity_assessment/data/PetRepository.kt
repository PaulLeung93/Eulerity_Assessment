// PetsRepository.kt
package com.example.eulerity_assessment.data

import com.example.eulerity_assessment.domain.model.Pet

interface PetRepository {
    suspend fun getPets(): List<Pet>
}
