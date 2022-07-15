package br.com.android.gitrepos.data.repository

import br.com.android.gitrepos.data.model.GitData
import br.com.android.gitrepos.data.repository.GitRepository
import br.com.android.gitrepos.data.services.GitApi

/**
 * Created by Carlos Souza on 15,julho,2022
 */
class GitRepositoryImpl(private val api: GitApi) : GitRepository {
    override suspend fun getGitRepos(page: Int): GitData = api.getRepos(page = page)
}