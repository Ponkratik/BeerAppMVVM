package com.ponkratov.beerapp.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Beer(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val tagline: String,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("brewers_tips")
    val brewersTips: String
)