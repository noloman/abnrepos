package com.nulltwenty.abnrepos.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.nulltwenty.abnrepos.data.GithubPagingSource
import com.nulltwenty.abnrepos.data.GithubPagingSource.Companion.NETWORK_PAGE_SIZE
import com.nulltwenty.abnrepos.data.di.IoCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryListRepositoryImpl @Inject constructor(
    @IoCoroutineDispatcher private val ioCoroutineDispatcher: CoroutineDispatcher,
    private val githubPagingSource: GithubPagingSource
) : RepositoryListRepository {
    override suspend fun fetchRepositoryList() = withContext(ioCoroutineDispatcher) {
        Pager(config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = true
        ), pagingSourceFactory = { githubPagingSource }).flow
    }
}
