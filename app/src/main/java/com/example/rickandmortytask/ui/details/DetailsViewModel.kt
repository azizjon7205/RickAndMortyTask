package com.example.rickandmortytask.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortytask.model.Results
import com.example.rickandmortytask.repository.MainRepository
import com.example.rickandmortytask.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(private val repository: MainRepository): ViewModel() {
    private val _detailState = MutableStateFlow<UiStateObject<Results>>(UiStateObject.EMPTY)
    val detailState = _detailState

    fun getSingleCharacter(id: Int) = viewModelScope.launch {
        _detailState.value = UiStateObject.LOADING
        try {
            val singleCharacter = repository.getSingleCharacter(id)
            _detailState.value = UiStateObject.SUCCESS(singleCharacter)
        } catch (e: Exception){
            _detailState.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }
}