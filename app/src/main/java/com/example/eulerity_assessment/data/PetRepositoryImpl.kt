package com.example.eulerity_assessment.data

import com.example.eulerity_assessment.domain.model.Pet
import javax.inject.Inject

class PetsRepositoryImpl @Inject constructor(
    private val petsAPI: PetsAPI
) : PetRepository {

    override suspend fun getPets(): List<Pet> {
        return petsAPI.getPets()
    }
}
