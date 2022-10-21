package com.nulltwenty.abnrepos.data.di

import com.nulltwenty.abnrepos.data.GithubPagingSource
import com.nulltwenty.abnrepos.data.repository.RepositoryListRepository
import com.nulltwenty.abnrepos.data.repository.RepositoryListRepositoryImpl
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
        githubPagingSource: GithubPagingSource
    ): RepositoryListRepository =
        RepositoryListRepositoryImpl(ioCoroutineDispatcher, githubPagingSource)
}