package com.example.citylistjetpack.domain.repository

import com.example.citylistjetpack.domain.model.CityList
import com.example.citylistjetpack.utility.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ICityRepository {
    suspend fun getCityList(isRefresh:Boolean) : Resource<List<CityList>>
    suspend fun insertCityListIntoDb(cities: List<CityList>)
    suspend fun fetchCityListFromDB(): List<CityList>
}