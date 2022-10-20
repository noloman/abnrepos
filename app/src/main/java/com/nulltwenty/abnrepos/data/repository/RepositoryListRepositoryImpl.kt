package com.nulltwenty.abnrepos.data.repository

import com.nulltwenty.abnrepos.domain.model.AbnRepo
import com.nulltwenty.abnrepos.domain.model.ResultOf
import com.nulltwenty.abnrepos.domain.safeApiCall
import data.api.model.RepositoryListResponse
import data.api.model.RepositoryListResponseElement
import data.api.service.GithubService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class RepositoryListRepositoryImpl @Inject constructor(
    private val ioCoroutineDispatcher: CoroutineDispatcher, private val githubService: GithubService
) : RepositoryListRepository {
    override suspend fun fetchRepositoryList(): ResultOf<List<AbnRepo>> =
        withContext(ioCoroutineDispatcher) {
            when (val apiResponse = safeApiCall { requestRepositoryList() }) {
                is ResultOf.Error -> return@withContext ResultOf.Error(apiResponse.exception)
                is ResultOf.Success -> return@withContext ResultOf.Success(apiResponse.data.toDomainModel())
            }
        }

    private suspend fun requestRepositoryList(): ResultOf<RepositoryListResponse> {
        val response: Response<RepositoryListResponse> = githubService.fetchRepos("", 1, 10)
        if (response.isSuccessful) {
            val body: RepositoryListResponse? = response.body()
            if (body != null) {
                return ResultOf.Success(body)
            }
        }
        return ResultOf.Error(IOException("Error fetching repositories ${response.code()} ${response.message()}"))
    }
}

private fun RepositoryListResponse.toDomainModel(): List<AbnRepo> = map { repoElement ->
    repoElement.toDomainModel()
}

private fun RepositoryListResponseElement.toDomainModel(): AbnRepo =
    AbnRepo(name, fullName, owner.avatarURL, description, htmlURL, visibility)
