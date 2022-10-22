package com.nulltwenty.abnrepos.ui

import androidx.paging.PagingData
import androidx.paging.flatMap
import app.cash.turbine.test
import com.nulltwenty.abnrepos.data.db.Repository
import com.nulltwenty.abnrepos.domain.GetAbnAmroRepositoriesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class RepositoryListViewModelTest {
    private lateinit var sut: RepositoryListViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val fakeRepository = Repository(
        1L,
        "fakeName",
        "fakeFullName",
        "fakeDescription",
        "fakeHtmlUrl",
        "fakeAvatarUrl",
        "fakeVisibility"
    )
    private val fakeRepositoryList = listOf(fakeRepository)
    private val useCaseMock: GetAbnAmroRepositoriesUseCase = mock {
        onBlocking { invoke() } doSuspendableAnswer {
            flowOf(PagingData.from(fakeRepositoryList))
        }
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `given a use case that returns a repository when fetching the repository list it should include it in the UI state`() =
        runTest {
            sut = RepositoryListViewModel(useCaseMock)
            sut.getRepositoryList()
            sut.uiState.test {
                val uiState: RepositoryListUiState = awaitItem()
                assertNull(uiState.error)
                assertNotNull(uiState.repositoryList)
                uiState.repositoryList?.flatMap { abnRepo ->
                    assertEquals(abnRepo, fakeRepository.toDomainModel())
                    return@flatMap fakeRepositoryList
                }
            }
        }
}