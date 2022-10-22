package com.nulltwenty.abnrepos.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.nulltwenty.abnrepos.data.api.model.RepositoryListResponseElement
import com.nulltwenty.abnrepos.data.api.service.GithubService
import com.nulltwenty.abnrepos.data.db.RemoteKeys
import com.nulltwenty.abnrepos.data.db.RepositoriesDatabase
import com.nulltwenty.abnrepos.data.db.Repository
import com.nulltwenty.abnrepos.data.di.IoCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

private const val STARTING_INDEX = 1L

@OptIn(ExperimentalPagingApi::class)
class GithubRemoteMediator(
    @IoCoroutineDispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val service: GithubService,
    private val repositoriesDatabase: RepositoriesDatabase
) : RemoteMediator<Int, Repository>() {
    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Repository>
    ): MediatorResult = withContext(coroutineDispatcher) {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                val prevKey = remoteKeys?.prevKey ?: return@withContext MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey ?: return@withContext MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }
        try {
            val response: Response<List<RepositoryListResponseElement>> =
                service.fetchRepos(page = page.toInt())
            val data = response.body()?.map {
                it.toDataModel()
            } ?: emptyList()
            val endOfPaginationReached = data.isEmpty()
            repositoriesDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    repositoriesDatabase.remoteKeysDao().clearRemoteKeys()
                    repositoriesDatabase.repositoriesDao().clearRepositories()
                }
                val prevKey = if (page == STARTING_INDEX) null else page.toInt() - 1
                val nextKey = if (endOfPaginationReached) null else page.toInt() + 1
                val keys = data.map {
                    RemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                repositoriesDatabase.remoteKeysDao().insertAll(keys)
                repositoriesDatabase.repositoriesDao().insertAll(data)
            }
            return@withContext MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return@withContext MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return@withContext MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Repository>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { repo ->
            // Get the remote keys of the last item retrieved
            repositoriesDatabase.remoteKeysDao().remoteKeysRepoId(repo.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Repository>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { repo ->
            // Get the remote keys of the first items retrieved
            repositoriesDatabase.remoteKeysDao().remoteKeysRepoId(repo.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Repository>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                repositoriesDatabase.remoteKeysDao().remoteKeysRepoId(repoId)
            }
        }
    }
}

fun RepositoryListResponseElement.toDataModel(): Repository =
    Repository(id, name, fullName, description, htmlURL, owner.avatarURL ?: "", visibility.name)