package ru.student.starwars.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.student.starwars.presentation.MainActivity
@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}