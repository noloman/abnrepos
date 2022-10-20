package com.nulltwenty.abnrepos.data.repository

import com.nulltwenty.abnrepos.domain.model.AbnRepo
import com.nulltwenty.abnrepos.domain.model.ResultOf

interface RepositoryListRepository {
    suspend fun fetchRepositoryList(): ResultOf<List<AbnRepo>>
}
