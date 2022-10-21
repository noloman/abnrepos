package com.nulltwenty.abnrepos.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nulltwenty.abnrepos.data.di.IoCoroutineDispatcher
import com.nulltwenty.abnrepos.domain.model.AbnRepo
import data.api.model.RepositoryListResponseElement
import data.api.service.GithubService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

private const val STARTING_KEY = 1L

class GithubPagingSource @Inject constructor(
    @IoCoroutineDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val githubService: GithubService
) : PagingSource<Long, AbnRepo>() {
    override fun getRefreshKey(state: PagingState<Long, AbnRepo>): Long? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, AbnRepo> =
        withContext(coroutineDispatcher) {
            try {
                val position = params.key ?: STARTING_KEY
                val response: Response<List<RepositoryListResponseElement>> =
                    githubService.fetchRepos(page = position.toInt())
                val data: List<AbnRepo> = response.body()?.map {
                    it.toDomainModel()
                } ?: emptyList()
                val nextKey = if (data.isEmpty()) {
                    null
                } else {
                    // initial load size = 3 * NETWORK_PAGE_SIZE
                    // ensure we're not requesting duplicating items, at the 2nd request
                    position + (params.loadSize / NETWORK_PAGE_SIZE)
                }
                LoadResult.Page(
                    data = data,
                    prevKey = if (position == STARTING_KEY) null else position - 1,
                    nextKey = nextKey
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}

private fun RepositoryListResponseElement.toDomainModel(): AbnRepo =
    AbnRepo(id, name, fullName, owner.avatarURL, description, htmlURL, visibility)