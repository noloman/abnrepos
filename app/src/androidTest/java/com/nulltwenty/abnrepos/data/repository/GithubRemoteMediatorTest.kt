package com.nulltwenty.abnrepos.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.nulltwenty.abnrepos.data.api.model.RepositoryListResponseElement
import com.nulltwenty.abnrepos.data.api.service.GithubService
import com.nulltwenty.abnrepos.data.db.RepositoriesDatabase
import com.nulltwenty.abnrepos.data.db.Repository
import com.nulltwenty.abnrepos.data.repository.RepositoriesRepositoryImpl.Companion.NETWORK_PAGE_SIZE
import com.nulltwenty.abnrepos.fakeRepositoryListResponseElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.mock
import retrofit2.Response
import java.io.IOException

@ExperimentalPagingApi
@OptIn(ExperimentalCoroutinesApi::class)
class GithubRemoteMediatorTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeGithubService: GithubService
    private val inMemoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(), RepositoriesDatabase::class.java
    ).build()

    private fun initDependencies(response: Response<List<RepositoryListResponseElement>>) {
        fakeGithubService = mock {
            onBlocking { fetchRepos(any()) } doSuspendableAnswer {
                response
            }
        }
    }

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
            initDependencies(Response.success(emptyList()))
            val sut = GithubRemoteMediator(testDispatcher, fakeGithubService, inMemoryDatabase)
            val pagingState = PagingState<Int, Repository>(
                listOf(), null, PagingConfig(NETWORK_PAGE_SIZE), 10
            )
            val result = sut.load(LoadType.REFRESH, pagingState)
            assertTrue(result is RemoteMediator.MediatorResult.Success)
            assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        }

    @Test
    fun whenNetworkServiceReturnsResults_MediatorResultSuccessEndOfPaginationReachedShouldBeFalse() =
        runTest {
            initDependencies(Response.success(listOf(fakeRepositoryListResponseElement)))
            val sut = GithubRemoteMediator(testDispatcher, fakeGithubService, inMemoryDatabase)
            val pagingState = PagingState<Int, Repository>(
                listOf(), null, PagingConfig(NETWORK_PAGE_SIZE), 10
            )
            val result = sut.load(LoadType.REFRESH, pagingState)
            assertTrue(result is RemoteMediator.MediatorResult.Success)
            assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        }

    @Test
    fun whenNetworkServiceReturnsAnException_itShouldReturnMediatorResultError() = runTest {
        fakeGithubService = mock {
            onBlocking { fetchRepos(any()) } doAnswer { throw IOException() }
        }
        val sut = GithubRemoteMediator(testDispatcher, fakeGithubService, inMemoryDatabase)
        val pagingState = PagingState<Int, Repository>(
            listOf(), null, PagingConfig(NETWORK_PAGE_SIZE), 10
        )
        val result = sut.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }
}