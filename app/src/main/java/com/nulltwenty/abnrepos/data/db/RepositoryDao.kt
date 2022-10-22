package com.nulltwenty.abnrepos.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repositories: List<Repository>)

    @Query("SELECT * FROM repositories")
    fun allRepositories(): PagingSource<Int, Repository>

    @Query("DELETE FROM repositories")
    suspend fun clearRepositories()
}