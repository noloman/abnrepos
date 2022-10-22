package com.nulltwenty.abnrepos.domain

import com.nulltwenty.abnrepos.data.di.DefaultCoroutineDispatcher
import com.nulltwenty.abnrepos.data.repository.RepositoriesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAbnAmroRepositoriesUseCase @Inject constructor(
    private val repositoriesRepository: RepositoriesRepository,
    @DefaultCoroutineDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() = withContext(coroutineDispatcher) {
        repositoriesRepository.fetchRepositories()
    }
}