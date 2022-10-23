package com.nulltwenty.abnrepos.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.nulltwenty.abnrepos.data.db.RepositoriesDatabase
import com.nulltwenty.abnrepos.data.di.IoCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoriesRepositoryImpl @Inject constructor(
    @IoCoroutineDispatcher private val ioCoroutineDispatcher: CoroutineDispatcher,
    private val database: RepositoriesDatabase,
    private val githubRemoteMediator: GithubRemoteMediator
) : RepositoriesRepository {
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun fetchRepositories() = withContext(ioCoroutineDispatcher) {
        return@withContext Pager(config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false
        ),
            remoteMediator = githubRemoteMediator,
            pagingSourceFactory = { database.repositoriesDao().allRepositories() }).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}
