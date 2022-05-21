package com.example.rickandmortytask.repository.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmortytask.repository.MainRepository
import com.example.rickandmortytask.ui.details.DetailsViewModel
import com.example.rickandmortytask.ui.main.MainViewModel
import com.example.rickandmortytask.ui.search.SearchViewModel
import java.lang.IllegalArgumentException

class MainViewModelFactory(private val repository: MainRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)){
            return DetailsViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)){
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}