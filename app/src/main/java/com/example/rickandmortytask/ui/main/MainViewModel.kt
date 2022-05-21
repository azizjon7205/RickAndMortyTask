package com.example.rickandmortytask.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortytask.model.Data
import com.example.rickandmortytask.model.Results
import com.example.rickandmortytask.repository.MainRepository
import com.example.rickandmortytask.utils.UiStateList
import com.example.rickandmortytask.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository):ViewModel() {

    private val _mainState = MutableStateFlow<UiStateObject<Data>>(UiStateObject.EMPTY)
    val mainState = _mainState

    fun getCharacters(page: Int) = viewModelScope.launch {
        _mainState.value = UiStateObject.LOADING
        try {
            val characters = repository.getCharacters(page)
            _mainState.value = UiStateObject.SUCCESS(characters)
        } catch (e: Exception){
            _mainState.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }
}