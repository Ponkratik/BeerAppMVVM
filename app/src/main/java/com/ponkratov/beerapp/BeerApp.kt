package com.ponkratov.beerapp;

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.ponkratov.beerapp.domain.db.AppDatabase

public class BeerApp : Application() {

    private var _appDatabase: AppDatabase? = null
    val appDatabase get() = requireNotNull(_appDatabase)

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext
        _appDatabase = Room
            .databaseBuilder(
                this,
                AppDatabase::class.java,
                "database-room"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    companion object {
        private var appContext: Context? = null

        fun getContext(): Context {
            return requireNotNull(appContext)
        }
    }
}

val Context.appDatabase: AppDatabase
    get() = when (this) {
        is BeerApp -> appDatabase
        else -> applicationContext.appDatabase
    }
