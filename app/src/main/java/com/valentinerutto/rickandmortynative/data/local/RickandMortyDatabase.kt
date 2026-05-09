package com.valentinerutto.rickandmortynative.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [CharacterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RickandMortyDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun remoteKeyDao(): CharacterRemoteKeyDao

    companion object Companion {
        @Volatile
        private var INSTANCE: RickandMortyDatabase? = null
        fun getDatabase(context: Context): RickandMortyDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, RickandMortyDatabase::class.java, "rickandmorty_database"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration(false).build()
                INSTANCE = instance
                instance
            }
        }
    }
}