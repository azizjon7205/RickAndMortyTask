package com.example.rickandmortytask.repository

import com.example.rickandmortytask.network.service.ApiService

class MainRepository(
    private val apiService: ApiService
) {
    suspend fun getCharacters(page: Int) = apiService.getCharacters(page)

    suspend fun getSingleCharacter(id: Int) = apiService.getSingleCharacter(id)

    suspend fun getFilteredCharacters(text: String) = apiService.getFilteredCharacters(text)
}