package com.nulltwenty.abnrepos.domain

import androidx.paging.PagingData
import androidx.paging.flatMap
import app.cash.turbine.test
import com.nulltwenty.abnrepos.data.db.Repository
import com.nulltwenty.abnrepos.data.repository.RepositoriesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class GetAbnAmroRepositoriesUseCaseTest {
    lateinit var sut: GetAbnAmroRepositoriesUseCase
    private val fakeRepository = Repository(
        1L,
        "fakeName",
        "fakeFullName",
        "fakeDescription",
        "fakeHtmlUrl",
        "fakeAvatarUrl",
        "fakeVisibility"
    )
    private val repositoryMock: RepositoriesRepository = mock {
        onBlocking { fetchRepositories() } doReturn flowOf(PagingData.from(listOf(fakeRepository)))
    }
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        sut = GetAbnAmroRepositoriesUseCase(repositoryMock, testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `given the repository containing one Github repository, when the use case fires, it should return the only repository available`() =
        runTest {
            sut.invoke().test {
                val pagingData = awaitItem()
                assertNotNull(pagingData)
                pagingData.flatMap {
                    assertEquals(it, fakeRepository)
                    return@flatMap listOf(it)
                }
                awaitComplete()
            }
        }
}