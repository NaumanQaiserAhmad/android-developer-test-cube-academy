package com.cube.cubeacademy.lib.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cube.cubeacademy.lib.di.Repository
import kotlinx.coroutines.launch

class ViewModel(private val repository: Repository) : ViewModel() {

    // Function to fetch all nominations
    fun getAllNominations() {
        viewModelScope.launch {
            val nominations = repository.getAllNominations()
            // Do something with the nominations (e.g., update LiveData)
        }
    }

    // Function to fetch all nominees
    fun getAllNominees() {
        viewModelScope.launch {
            val nominees = repository.getAllNominees()
            // Do something with the nominees (e.g., update LiveData)
        }
    }

    // Function to create a new nomination
    fun createNomination(nomineeId: String, reason: String, process: String) {
        viewModelScope.launch {
            val createdNomination = repository.createNomination(nomineeId, reason, process)
            // Do something with the created nomination (e.g., update LiveData)
        }
    }
}
