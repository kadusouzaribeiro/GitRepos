package br.com.android.gitrepos.data.remote

import br.com.android.gitrepos.data.remote.dto.GitData

/**
 * Created by Carlos Souza on 15,junho,2022
 */

class GitRepository(private val api: GitApi) {

    suspend fun getGitRepos(page: Int): GitData = api.getRepos(page = page)
}