package com.nulltwenty.abnrepos.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.nulltwenty.abnrepos.data.api.service.GithubService
import com.nulltwenty.abnrepos.data.db.RepoDatabase
import com.nulltwenty.abnrepos.domain.model.AbnRepo

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator(
    private val service: GithubService,
    private val repoDatabase: RepoDatabase
) : RemoteMediator<Long, AbnRepo>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Long, AbnRepo>
    ): MediatorResult {
        TODO("Not yet implemented")
    }
}