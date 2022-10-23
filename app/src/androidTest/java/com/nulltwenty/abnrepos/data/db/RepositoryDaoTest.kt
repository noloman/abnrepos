package com.nulltwenty.abnrepos.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.nulltwenty.abnrepos.getFakeRepositoryList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositoryDaoTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var repositoryDao: RepositoryDao
    private val inMemoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(), RepositoriesDatabase::class.java
    ).build()

    @Before
    fun setup() {
        repositoryDao = inMemoryDatabase.repositoriesDao()
    }

    @Test
    fun givenAListOfRepositories_whenInsertingThemIntoDatabase_itShouldRetrieveThem() = runTest {
        repositoryDao.insertAll(getFakeRepositoryList(1))

        val allRepositories: PagingSource<Int, Repository> = repositoryDao.allRepositories()

        assertNotNull(allRepositories)
    }
}