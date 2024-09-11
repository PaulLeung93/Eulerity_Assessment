package com.example.eulerity_assessment.domain

import com.example.eulerity_assessment.domain.model.Pet
import javax.inject.Inject

class SortPetsUseCaseImpl @Inject constructor() : SortPetsUseCase {
    override fun sortPets(pets: List<Pet>, ascending: Boolean): List<Pet> {
        return if (ascending) {
            pets.sortedBy { it.title }
        } else {
            pets.sortedByDescending { it.title }
        }
    }
}
