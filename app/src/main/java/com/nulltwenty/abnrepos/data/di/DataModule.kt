package com.nulltwenty.abnrepos.data.di

import com.nulltwenty.abnrepos.data.api.service.GithubService
import com.nulltwenty.abnrepos.data.db.RepositoriesDatabase
import com.nulltwenty.abnrepos.data.repository.GithubRemoteMediator
import com.nulltwenty.abnrepos.data.repository.RepositoriesRepository
import com.nulltwenty.abnrepos.data.repository.RepositoriesRepositoryImpl
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
        database: RepositoriesDatabase,
        githubRemoteMediator: GithubRemoteMediator
    ): RepositoriesRepository =
        RepositoriesRepositoryImpl(ioCoroutineDispatcher, database, githubRemoteMediator)

    @Provides
    fun provideGithubRemoteMediator(
        @IoCoroutineDispatcher ioCoroutineDispatcher: CoroutineDispatcher,
        service: GithubService,
        database: RepositoriesDatabase
    ) = GithubRemoteMediator(ioCoroutineDispatcher, service, database)
}