package ru.student.starwars.presentation

import android.app.Application
import ru.student.starwars.di.ApplicationComponent
import ru.student.starwars.di.DaggerApplicationComponent

class StarApplication : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
    }
}