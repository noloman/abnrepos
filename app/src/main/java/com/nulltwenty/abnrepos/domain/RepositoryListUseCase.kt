package com.nulltwenty.abnrepos.domain

import com.nulltwenty.abnrepos.data.repository.RepositoryListRepository
import com.nulltwenty.abnrepos.domain.model.AbnRepo
import com.nulltwenty.abnrepos.domain.model.ResultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryListUseCase @Inject constructor(private val repositoryListRepository: RepositoryListRepository) {
    suspend operator fun invoke(): ResultOf<List<AbnRepo>> = withContext(Dispatchers.IO) {
        repositoryListRepository.fetchRepositoryList()
    }
}