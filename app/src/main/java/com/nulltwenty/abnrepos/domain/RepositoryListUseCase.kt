package com.nulltwenty.abnrepos.domain

import com.nulltwenty.abnrepos.data.di.DefaultCoroutineDispatcher
import com.nulltwenty.abnrepos.data.repository.RepositoryListRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryListUseCase @Inject constructor(
    private val repositoryListRepository: RepositoryListRepository,
    @DefaultCoroutineDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() = withContext(coroutineDispatcher) {
        repositoryListRepository.fetchRepositoryList()
    }
}