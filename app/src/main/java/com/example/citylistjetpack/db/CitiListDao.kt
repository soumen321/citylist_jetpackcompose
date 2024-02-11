package com.example.citylistjetpack.db
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.citylistjetpack.domain.model.CityList
import kotlinx.coroutines.flow.Flow

@Dao
interface CitiListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(city: List<CityList>)

    @Query("SELECT * FROM CityList")
    fun getCityList(): Flow<List<CityList>>

    @Query("DELETE FROM CityList")
    suspend fun deleteAll()
}