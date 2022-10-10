package com.ponkratov.beerapp.model

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

    fun provideBeerApi(): BeerApi = beerApi
}