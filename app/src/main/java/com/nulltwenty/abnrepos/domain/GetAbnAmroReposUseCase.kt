package com.nulltwenty.abnrepos.domain

import com.nulltwenty.abnrepos.data.di.DefaultCoroutineDispatcher
import com.nulltwenty.abnrepos.data.repository.ReposRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAbnAmroReposUseCase @Inject constructor(
    private val reposRepository: ReposRepository,
    @DefaultCoroutineDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke() = withContext(coroutineDispatcher) {
        reposRepository.fetchRepos()
    }
}