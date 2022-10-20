package com.nulltwenty.abnrepos.domain

import com.nulltwenty.abnrepos.data.repository.RepositoryListRepository
import com.nulltwenty.abnrepos.domain.model.ResultOf
import data.api.model.RepositoryListResponseElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryListUseCase constructor(private val repositoryListRepository: RepositoryListRepository) {
    suspend operator fun invoke(): ResultOf<List<RepositoryListResponseElement>> =
        withContext(Dispatchers.IO) {
            repositoryListRepository.fetchRepositoryList()
        }
}