package com.nulltwenty.abnrepos.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.nulltwenty.abnrepos.data.GithubPagingSource
import com.nulltwenty.abnrepos.data.di.IoCoroutineDispatcher
import com.nulltwenty.abnrepos.domain.model.AbnRepo
import data.api.model.RepositoryListResponse
import data.api.model.RepositoryListResponseElement
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryListRepositoryImpl @Inject constructor(
    @IoCoroutineDispatcher private val ioCoroutineDispatcher: CoroutineDispatcher,
    private val githubPagingSource: GithubPagingSource
) : RepositoryListRepository {
    override suspend fun fetchRepositoryList() = withContext(ioCoroutineDispatcher) {
        Pager(config = PagingConfig(
            pageSize = 1, enablePlaceholders = true
        ), pagingSourceFactory = { githubPagingSource }).flow
    }
}

fun RepositoryListResponse.toDomainModel(): List<AbnRepo> = map { repoElement ->
    repoElement.toDomainModel()
}

fun RepositoryListResponseElement.toDomainModel(): AbnRepo =
    AbnRepo(id, name, fullName, owner.avatarURL, description, htmlURL, visibility)
