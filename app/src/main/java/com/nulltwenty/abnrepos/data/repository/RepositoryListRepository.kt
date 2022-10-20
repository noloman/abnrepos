package com.nulltwenty.abnrepos.data.repository

import com.nulltwenty.abnrepos.domain.model.ResultOf
import data.api.model.RepositoryListResponse

interface RepositoryListRepository {
    suspend fun fetchRepositoryList(): ResultOf<RepositoryListResponse>
}
