package com.example.citylistjetpack.domain.repository

import com.example.citylistjetpack.domain.model.CityList
import com.example.citylistjetpack.utility.Resource
import retrofit2.Response

interface ICityRepository {
    suspend fun getCityList() : Resource<List<CityList>>
}