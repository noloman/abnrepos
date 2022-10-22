package com.nulltwenty.abnrepos.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.nulltwenty.abnrepos.fakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RemoteKeyDaoTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var remoteKeyDao: RemoteKeysDao
    private val inMemoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(), RepositoriesDatabase::class.java
    ).build()

    @Before
    fun setup() {
        remoteKeyDao = inMemoryDatabase.remoteKeysDao()
    }

    @Test
    fun insertAndLoadById() = runTest {
        val remoteKey = RemoteKeys(1L, null, 2)
        val remoteKeys = listOf(remoteKey)
        remoteKeyDao.insertAll(remoteKeys)

        val loadedRemoteKeys = remoteKeyDao.remoteKeysRepoId(fakeRepository.id)

        assertNotNull(loadedRemoteKeys)
        assertEquals(loadedRemoteKeys?.repoId, fakeRepository.id)
        assertEquals(loadedRemoteKeys?.nextKey, remoteKey.nextKey)
        assertEquals(loadedRemoteKeys?.prevKey, remoteKey.prevKey)
    }

    @Test
    fun insertAndDelete() = runTest {
        val remoteKey = RemoteKeys(1L, null, 2)
        val remoteKeys = listOf(remoteKey)
        remoteKeyDao.insertAll(remoteKeys)

        remoteKeyDao.clearRemoteKeys()

        val loadedRemoteKeys = remoteKeyDao.remoteKeysRepoId(fakeRepository.id)
        assertNull(loadedRemoteKeys)
    }
}