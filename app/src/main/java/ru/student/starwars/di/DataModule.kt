package ru.student.starwars.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.student.starwars.data.network.ApiFactory
import ru.student.starwars.data.network.ApiService
import ru.student.starwars.data.repository.CharactersRepositoryImpl
import ru.student.starwars.data.repository.StarshipsRepositoryImpl
import ru.student.starwars.data.room.dao.CharactersDao
import ru.student.starwars.data.room.dao.StarshipsDao
import ru.student.starwars.data.room.db.AppDatabase
import ru.student.starwars.domain.repository.CharactersRepository
import ru.student.starwars.domain.repository.StarshipsRepository

@Module
interface DataModule {

    @Binds
    fun bindCharactersRepository(repositoryImpl: CharactersRepositoryImpl): CharactersRepository

    @Binds
    fun bindStarshipsRepository(repositoryImpl: StarshipsRepositoryImpl): StarshipsRepository

    companion object {
        @Provides
        fun provideCharactersDao(application: Application): CharactersDao {
            return AppDatabase.getInstance(application).charactersDao()
        }

        @Provides
        fun provideStarshipDao(application: Application): StarshipsDao {
            return AppDatabase.getInstance(application).starshipsDao()
        }

        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

    }
}