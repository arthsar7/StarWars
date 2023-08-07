package ru.student.starwars.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.student.starwars.data.network.ApiFactory
import ru.student.starwars.data.network.ApiService
import ru.student.starwars.data.repository.StarRepositoryImpl
import ru.student.starwars.data.room.AppDatabase
import ru.student.starwars.data.room.StarDao
import ru.student.starwars.domain.repository.StarRepository

@Module
interface DataModule {

    @Binds
    fun bindStarRepository(repositoryImpl: StarRepositoryImpl): StarRepository

    companion object {
        @Provides
        fun provideStarDao(application: Application): StarDao {
            return AppDatabase.getInstance(application).starDao()
        }

        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}