package com.example.citylistjetpack.di

import android.content.Context
import androidx.room.Room
import com.example.citylistjetpack.data.remote.ICityRemoteApi
import com.example.citylistjetpack.data.remote.MockCityRemoteApi
import com.example.citylistjetpack.data.repository.CityRepositoryImpl
import com.example.citylistjetpack.db.CityDatabase
import com.example.citylistjetpack.domain.repository.ICityRepository
import com.example.citylistjetpack.domain.usecase.UserCaseCityList
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun providesRoomDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        CityDatabase::class.java,
        "city.db"
    ).build()

    @Singleton
    @Provides
    fun providesCityRepository(
        api: ICityRemoteApi,
        dao: CityDatabase
    ) : ICityRepository =
        CityRepositoryImpl(api,dao)

   // @Singleton
    @Provides
    fun providesCityUseCase(repo: ICityRepository) = UserCaseCityList(
        repo
    )
}