package com.example.citylistjetpack.data.remote

import com.example.citylistjetpack.domain.model.CityList

class MockCityRemoteApi  : ICityRemoteApi {
    override suspend fun getData(): List<CityList> {
        return FakeJsonDataInject.getCitListFromApi()
    }
}