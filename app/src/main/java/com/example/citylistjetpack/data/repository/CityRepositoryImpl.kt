package com.example.citylistjetpack.data.repository
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.citylistjetpack.data.remote.ICityRemoteApi
import com.example.citylistjetpack.db.CityDatabase
import com.example.citylistjetpack.domain.model.CityList
import com.example.citylistjetpack.domain.repository.ICityRepository
import com.example.citylistjetpack.utility.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class CityRepositoryImpl(
    private val api: ICityRemoteApi,
    private val dao: CityDatabase
): ICityRepository {
    override suspend fun getCityList(isRefresh:Boolean) : Resource<List<CityList>>{
        return try {

            val cachedStates = fetchCityListFromDB()
            if (cachedStates.isEmpty() || isRefresh) {
                Log.e("getCityList","From API")
                val data = api.getData()
                insertCityListIntoDb(data)
                Resource.Success(data)
            } else {
                Log.e("getCityList","From DB")
                Resource.Success(cachedStates)
            }
        } catch (e: Exception) {
            Resource.Error("Failed to fetch city list: ${e.message}")
        }
    }

    override suspend fun insertCityListIntoDb(cities: List<CityList>) {
        dao.cityDao.upsert(cities)
    }

    override suspend fun fetchCityListFromDB(): List<CityList> {
        return dao.cityDao.getCityList().firstOrNull() ?: emptyList()
    }


}