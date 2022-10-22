package com.nulltwenty.abnrepos.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Repository::class, RemoteKeys::class], version = 1, exportSchema = false
)
abstract class RepositoriesDatabase : RoomDatabase() {
    abstract fun repositoriesDao(): RepositoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: RepositoriesDatabase? = null
        fun getInstance(context: Context): RepositoriesDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, RepositoriesDatabase::class.java, "GithubRepos.db"
        ).build()
    }
}