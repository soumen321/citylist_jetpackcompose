package com.example.citylistjetpack.di

import com.example.citylistjetpack.data.remote.ICityRemoteApi
import com.example.citylistjetpack.data.remote.MockCityRemoteApi
import com.example.citylistjetpack.data.repository.CityRepositoryImpl
import com.example.citylistjetpack.domain.repository.ICityRepository
import com.example.citylistjetpack.domain.usecase.UserCaseCityList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CityListDI {

    @Singleton
    @Provides
    fun provideCityRemoteApi(): ICityRemoteApi {
        return MockCityRemoteApi() // Or whatever implementation you have
    }



    @Singleton
    @Provides
    fun providesCityRepository(
        api: ICityRemoteApi
    ) : ICityRepository =
        CityRepositoryImpl(api)

   // @Singleton
    @Provides
    fun providesCityUseCase(repo: ICityRepository) = UserCaseCityList(
        repo
    )
}