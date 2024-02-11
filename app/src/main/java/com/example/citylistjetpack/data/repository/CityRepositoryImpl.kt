package com.example.citylistjetpack.data.repository
import com.example.citylistjetpack.data.remote.ICityRemoteApi
import com.example.citylistjetpack.domain.model.CityList
import com.example.citylistjetpack.domain.repository.ICityRepository
import com.example.citylistjetpack.utility.Resource
import retrofit2.Response
import javax.inject.Inject

class CityRepositoryImpl (
    private val  api:ICityRemoteApi
): ICityRepository {
    override suspend fun getCityList() : Resource<List<CityList>>{
        return try {
            Resource.Success(api.getData())
        } catch (e: Exception) {
            Resource.Error("Failed to fetch states: ${e.message}")
        }
    }
}