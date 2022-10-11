package com.ponkratov.beerapp.domain.db

import androidx.room.*
import com.ponkratov.beerapp.model.entity.Beer

@Dao
interface BeerDao {
    @Query("SELECT * FROM beer")
    fun getAll(): List<Beer>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(beers: List<Beer>)

    @Query("DELETE FROM beer")
    fun deleteAll()
}