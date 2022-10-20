package com.nulltwenty.abnrepos.domain

import com.nulltwenty.abnrepos.data.di.DefaultCoroutineDispatcher
import com.nulltwenty.abnrepos.data.repository.RepositoryListRepository
import com.nulltwenty.abnrepos.domain.model.AbnRepo
import com.nulltwenty.abnrepos.domain.model.ResultOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryListUseCase @Inject constructor(
    private val repositoryListRepository: RepositoryListRepository,
    @DefaultCoroutineDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): ResultOf<List<AbnRepo>> = withContext(coroutineDispatcher) {
        repositoryListRepository.fetchRepositoryList()
    }
}