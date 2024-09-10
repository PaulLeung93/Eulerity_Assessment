package com.example.eulerity_assessment.domain

import com.example.eulerity_assessment.domain.model.Pet

interface SortPetsUseCase {
    fun sortPets(pets: List<Pet>, ascending: Boolean): List<Pet>
}
