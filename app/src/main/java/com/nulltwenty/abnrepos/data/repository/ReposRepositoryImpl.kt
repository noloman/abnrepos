package com.nulltwenty.abnrepos.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.nulltwenty.abnrepos.data.api.service.GithubService
import com.nulltwenty.abnrepos.data.db.RepositoriesDatabase
import com.nulltwenty.abnrepos.data.di.IoCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReposRepositoryImpl @Inject constructor(
    @IoCoroutineDispatcher private val ioCoroutineDispatcher: CoroutineDispatcher,
    private val service: GithubService,
    private val database: RepositoriesDatabase
) : ReposRepository {
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun fetchRepos() = withContext(ioCoroutineDispatcher) {
        return@withContext Pager(config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false
        ),
            remoteMediator = GithubRemoteMediator(ioCoroutineDispatcher, service, database),
            pagingSourceFactory = { database.reposDao().allRepositories() }).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}
