package com.example.eulerity_assessment.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eulerity_assessment.data.Pet
import com.example.eulerity_assessment.data.PetsRepository
import com.example.eulerity_assessment.domain.SortPetsUseCase
import kotlinx.coroutines.launch

class PetsViewModel {
    class PetsViewModel(
        private val repository: PetsRepository,
        private val sortPetsUseCase: SortPetsUseCase.SortPetsUseCase
    ) : ViewModel() {

        private val _pets = MutableLiveData<List<Pet>>()
        val pets: LiveData<List<Pet>> = _pets

        init {
            fetchPets()
        }

        private fun fetchPets() {
            viewModelScope.launch {
                try {
                    val petsList = repository.getPets()
                    _pets.value = petsList
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }

        fun sortPets(ascending: Boolean) {
            val sortedList = sortPetsUseCase.sortPets(_pets.value ?: emptyList(), ascending)
            _pets.value = sortedList
        }

        fun filterPets(query: String) {
            _pets.value = _pets.value?.filter {
                it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true)
            }
        }
    }
}