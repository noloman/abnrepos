package com.nulltwenty.abnrepos.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.nulltwenty.abnrepos.data.api.service.GithubService
import com.nulltwenty.abnrepos.data.db.RepoDatabase
import com.nulltwenty.abnrepos.data.db.Repository
import com.nulltwenty.abnrepos.data.repository.ReposRepositoryImpl.Companion.NETWORK_PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.mock
import retrofit2.Response

@ExperimentalPagingApi
@OptIn(ExperimentalCoroutinesApi::class)
class GithubRemoteMediatorTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val fakeGithubService: GithubService = mock {
        onBlocking { fetchRepos(any(), any()) } doSuspendableAnswer {
            Response.success((listOf()))
        }
    }
    private val inMemoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(), RepoDatabase::class.java
    ).build()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        inMemoryDatabase.clearAllTables()
    }

    @Test
    fun whenNetworkServiceReturnsEmptyResults_MediatorResultSuccessEndOfPaginationReachedShouldBeTrue() =
        runTest {
            val sut = GithubRemoteMediator(testDispatcher, fakeGithubService, inMemoryDatabase)
            val pagingState = PagingState<Int, Repository>(
                listOf(), null, PagingConfig(NETWORK_PAGE_SIZE), 10
            )
            val result = sut.load(LoadType.REFRESH, pagingState)
            assertTrue(result is RemoteMediator.MediatorResult.Success)
            assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        }
}