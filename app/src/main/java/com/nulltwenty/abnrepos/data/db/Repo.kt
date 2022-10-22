package com.nulltwenty.abnrepos.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "repos")
data class Repo(
    @PrimaryKey val id: Long,
    val name: String,
    @field:Json(name = "full_name") val fullName: String,
    val description: String?,
    @field:Json(name = "html_url") val url: String,
)
