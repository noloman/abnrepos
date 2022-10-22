package com.nulltwenty.abnrepos.data.repository

import androidx.paging.PagingData
import com.nulltwenty.abnrepos.data.db.Repository
import kotlinx.coroutines.flow.Flow

interface RepositoriesRepository {
    suspend fun fetchRepositories(): Flow<PagingData<Repository>>
}
