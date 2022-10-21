package data.api.service

import data.api.model.RepositoryListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GithubService {
    @GET("repos")
    suspend fun fetchRepos(
        @Header("Authorization") accessToken: String = "token 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",
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = PER_PAGE
    ): Response<RepositoryListResponse>

    companion object {
        const val BASE_URL = "https://api.github.om/users/abnamrocoesd/"
        const val PER_PAGE = 10
    }
}