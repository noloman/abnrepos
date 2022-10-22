package com.nulltwenty.abnrepos.data.di

import android.content.Context
import com.nulltwenty.abnrepos.data.db.RepoDao
import com.nulltwenty.abnrepos.data.db.RepoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): RepoDatabase =
        RepoDatabase.getInstance(appContext)

    @Provides
    fun provideRepoDao(appDatabase: RepoDatabase): RepoDao = appDatabase.reposDao()
}