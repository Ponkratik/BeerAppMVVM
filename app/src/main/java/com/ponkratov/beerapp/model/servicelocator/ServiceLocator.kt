package com.ponkratov.beerapp.model.servicelocator

import androidx.core.content.ContentProviderCompat.requireContext
import com.ponkratov.beerapp.BeerApp
import com.ponkratov.beerapp.appDatabase
import com.ponkratov.beerapp.domain.db.BeerDao
import com.ponkratov.beerapp.model.repository.BeerApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ServiceLocator {
    private const val baseUrl = "https://api.punkapi.com/v2/"

    private val retorfit by lazy {
        val client = OkHttpClient.Builder()
            .build()

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private val beerApi by lazy {
        retorfit.create<BeerApi>()
    }

    private val beerDao by lazy {
        BeerApp.getContext().appDatabase.beerDao()
    }

    fun provideBeerApi(): BeerApi = beerApi

    fun provideBeerDao(): BeerDao = beerDao
}