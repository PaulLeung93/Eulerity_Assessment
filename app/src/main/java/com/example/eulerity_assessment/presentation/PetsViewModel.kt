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
    private val _originalPets = mutableListOf<Pet>()  //Used to keep track of the original pet list when refreshing
    val pets: LiveData<List<Pet>> = _pets

    init {
        fetchPets()
    }

    private fun fetchPets() {
        viewModelScope.launch {
            try {
                val petsList = repository.getPets()
                _originalPets.clear()
                _originalPets.addAll(petsList)
                _pets.value = petsList
            } catch (e: Exception) {
                Log.e("PetsViewModel", "error: ${e.message}", e)
            }
        }
    }

    fun filterPets(query: String) {
        if (query.isEmpty()) {
            //Reset list
            _pets.value = _originalPets
        }
        else {
            //Filter based on query in searchbar
            _pets.value = _originalPets.filter {
                it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true)
            }
        }
    }

    fun sortPetsAscending() {
        val sortedList = sortPetsUseCase.sortPets(_pets.value ?: emptyList(), ascending = true)
        _pets.postValue(sortedList)
    }

    fun sortPetsDescending() {
        val sortedList = sortPetsUseCase.sortPets(_pets.value ?: emptyList(), ascending = false)
        _pets.postValue(sortedList)
    }

}
