package com.example.rickandmortytask.model

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("info"    ) var info    : Info?              = Info(),
    @SerializedName("results" ) var results : ArrayList<Results> = arrayListOf()
)
