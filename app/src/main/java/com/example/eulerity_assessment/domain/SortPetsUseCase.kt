package com.example.eulerity_assessment.domain

import com.example.eulerity_assessment.data.Pet

class SortPetsUseCase {
    class SortPetsUseCase {
        fun sortPets(pets: List<Pet>, ascending: Boolean): List<Pet> {
            return if (ascending) {
                pets.sortedBy { it.title }
            } else {
                pets.sortedByDescending { it.title }
            }
        }
    }
}