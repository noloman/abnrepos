package com.nulltwenty.abnrepos.data.di

import com.nulltwenty.abnrepos.data.repository.RepositoryListRepository
import com.nulltwenty.abnrepos.data.repository.RepositoryListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import data.api.service.GithubService
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun githubRepositoriesRepository(
        @IoCoroutineDispatcher ioCoroutineDispatcher: CoroutineDispatcher,
        githubService: GithubService
    ): RepositoryListRepository = RepositoryListRepositoryImpl(ioCoroutineDispatcher, githubService)
}