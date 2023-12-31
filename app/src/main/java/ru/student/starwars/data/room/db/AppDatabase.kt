package ru.student.starwars.data.room.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.student.starwars.data.room.dao.CharactersDao
import ru.student.starwars.data.room.dao.StarshipsDao
import ru.student.starwars.data.room.model.CharacterDbModel
import ru.student.starwars.data.room.model.StarshipDbModel

@Database(entities = [CharacterDbModel::class, StarshipDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun charactersDao(): CharactersDao
    abstract fun starshipsDao(): StarshipsDao
    companion object {
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "star_items.db"
        fun getInstance(application: Application): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
}