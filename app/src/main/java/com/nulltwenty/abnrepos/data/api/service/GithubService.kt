package com.nulltwenty.abnrepos.data.api.service

import com.nulltwenty.abnrepos.data.api.model.RepositoryListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {
//    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("repos")
    suspend fun fetchRepos(@Query("page") page: Int): Response<RepositoryListResponse>

    companion object {
        const val BASE_URL = "https://api.github.com/users/abnamrocoesd/"
    }
}