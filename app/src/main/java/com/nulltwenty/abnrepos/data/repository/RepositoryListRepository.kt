package com.nulltwenty.abnrepos.data.repository

import androidx.paging.PagingData
import com.nulltwenty.abnrepos.data.db.Repo
import kotlinx.coroutines.flow.Flow

interface RepositoryListRepository {
    suspend fun fetchRepositoryList(): Flow<PagingData<Repo>>
}
