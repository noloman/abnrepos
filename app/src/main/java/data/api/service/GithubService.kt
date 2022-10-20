package data.api.service

import data.api.model.RepositoryListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {
    @GET("repos")
    suspend fun fetchRepos(
        @Query("q") query: String, @Query("page") page: Int, @Query("per_page") itemsPerPage: Int
    ): Response<RepositoryListResponse>

    companion object {
        const val BASE_URL = "https://api.github.com/users/abnamrocoesd/"
    }
}