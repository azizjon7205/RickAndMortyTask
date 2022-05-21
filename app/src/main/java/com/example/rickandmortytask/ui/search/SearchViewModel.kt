package com.example.rickandmortytask.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortytask.model.Data
import com.example.rickandmortytask.model.Results
import com.example.rickandmortytask.repository.MainRepository
import com.example.rickandmortytask.utils.UiStateList
import com.example.rickandmortytask.utils.UiStateObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: MainRepository):ViewModel() {

    private val _searchState = MutableStateFlow<UiStateObject<Data>>(UiStateObject.EMPTY)
    val searchState = _searchState


    fun getFilteredCharacters(text: String) = viewModelScope.launch {
        _searchState.value = UiStateObject.LOADING
        try {
            val filteredCharacters = repository.getFilteredCharacters(text)
            _searchState.value = UiStateObject.SUCCESS(filteredCharacters)
        } catch (e: Exception){
            _searchState.value = UiStateObject.ERROR(e.localizedMessage ?: "No Connection")
        }
    }
}