package br.com.android.gitrepos.data.remote

import br.com.android.gitrepos.data.remote.dto.GitData
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Carlos Souza on 15,junho,2022
 */
interface GitApi {
    @GET("search/repositories")
    suspend fun getRepos(
        @Query("q") search: String = "language:kotlin",
        @Query("sort") sort: String = "stars",
        @Query("per_page") per_page: Int = 50,
        @Query("page") page: Int = 1
    ) : GitData
}