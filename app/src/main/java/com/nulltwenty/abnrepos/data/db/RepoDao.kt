package com.nulltwenty.abnrepos.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repositories: List<Repository>)

    @Query("SELECT * FROM repos")
    fun allRepos(): PagingSource<Int, Repository>

    @Query("DELETE FROM repos")
    suspend fun clearRepos()
}