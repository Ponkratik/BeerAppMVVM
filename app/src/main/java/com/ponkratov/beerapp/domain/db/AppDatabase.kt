package com.ponkratov.beerapp.domain.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ponkratov.beerapp.model.entity.Beer

@Database(entities = [Beer::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun beerDao(): BeerDao
}