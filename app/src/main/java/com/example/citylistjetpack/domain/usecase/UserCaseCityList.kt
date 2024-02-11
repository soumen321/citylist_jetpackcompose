package com.example.citylistjetpack.domain.usecase

import com.example.citylistjetpack.R
import com.example.citylistjetpack.domain.model.CityList
import com.example.citylistjetpack.domain.repository.ICityRepository
import com.example.citylistjetpack.utility.Resource
import javax.inject.Inject

class UserCaseCityList @Inject constructor(
    private val repository : ICityRepository,
) {

    suspend operator fun invoke(isRefresh:Boolean = false): Resource<List<CityList>> {
        return repository.getCityList(isRefresh)
    }
}