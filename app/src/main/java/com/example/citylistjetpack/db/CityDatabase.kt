package com.example.citylistjetpack.db
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.citylistjetpack.domain.model.CityList

@Database(
    entities = [CityList::class],
    version = 1
)

abstract class CityDatabase : RoomDatabase() {

    abstract val cityDao: CitiListDao
   // abstract val exclusionDao:ExclusionDao
}