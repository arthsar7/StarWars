package ru.student.starwars.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.student.starwars.presentation.FavoriteViewModel
import ru.student.starwars.presentation.MainViewModel

@Module
interface ViewModelModule {
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    @Binds
    fun bindFavoriteViewModel(favoriteViewModel: FavoriteViewModel): ViewModel
}