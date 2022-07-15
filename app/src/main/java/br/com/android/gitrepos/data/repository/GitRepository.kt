package br.com.android.gitrepos.data.repository

import br.com.android.gitrepos.data.model.GitData

/**
 * Created by Carlos Souza on 15,julho,2022
 */
interface GitRepository {
    suspend fun getGitRepos() : GitData
}