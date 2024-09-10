package com.example.eulerity_assessment.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eulerity_assessment.data.PetRepository
import com.example.eulerity_assessment.domain.model.Pet
import com.example.eulerity_assessment.domain.SortPetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class PetsViewModel @Inject constructor(
    private val repository: PetRepository,
    private val sortPetsUseCase: SortPetsUseCase
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
                Log.e("PetsViewModel", "error: ${e.message}", e)
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
