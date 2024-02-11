package com.example.citylistjetpack.data.remote

import com.example.citylistjetpack.domain.model.CityList
import retrofit2.Response

interface ICityRemoteApi {
    suspend fun getData(): List<CityList>
}