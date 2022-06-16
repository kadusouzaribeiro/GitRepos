package br.com.android.gitrepos.data.remote

/**
 * Created by Carlos Souza on 15,junho,2022
 */

class GitRepository(private val api: GitApi) {

    suspend fun getGitRepos(page: Int) = api.getRepos(page = page)

}