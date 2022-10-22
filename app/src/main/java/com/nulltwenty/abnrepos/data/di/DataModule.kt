package com.nulltwenty.abnrepos.data.di

import com.nulltwenty.abnrepos.data.api.service.GithubService
import com.nulltwenty.abnrepos.data.db.RepositoriesDatabase
import com.nulltwenty.abnrepos.data.repository.ReposRepository
import com.nulltwenty.abnrepos.data.repository.ReposRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun githubRepositoriesRepository(
        @IoCoroutineDispatcher ioCoroutineDispatcher: CoroutineDispatcher,
        service: GithubService,
        database: RepositoriesDatabase
    ): ReposRepository =
        ReposRepositoryImpl(ioCoroutineDispatcher, service, database)
}