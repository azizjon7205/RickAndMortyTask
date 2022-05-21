package com.example.rickandmortytask.network.service

import com.example.rickandmortytask.model.Data
import com.example.rickandmortytask.model.Results
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService{
    @GET("character/")
    suspend fun getCharacters(@Query("page") page: Int = 1): Data

    @GET("character/{id}")
    suspend fun getSingleCharacter(@Path("id") id: Int): Results

    @GET("character/")
    suspend fun getFilteredCharacters(@Query("name") text: String): Data


}
