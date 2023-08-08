package ru.student.starwars.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.student.starwars.presentation.FavoriteFragment
import ru.student.starwars.presentation.MainActivity
import ru.student.starwars.presentation.MainFragment

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: MainFragment)

    fun inject(fragment: FavoriteFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}