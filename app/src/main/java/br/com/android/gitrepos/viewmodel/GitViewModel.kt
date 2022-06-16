package br.com.android.gitrepos.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.android.gitrepos.data.ApiResponse
import br.com.android.gitrepos.data.remote.GitRepository
import br.com.android.gitrepos.data.remote.dto.GitData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * Created by Carlos Souza on 15,junho,2022
 */
class GitViewModel(private val repository: GitRepository) : ViewModel() {

    val state = MutableLiveData<ApiResponse<GitData?>>()

    fun getRepos(page: Int) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            state.postValue(ApiResponse.error(null, throwable.message ?: "Error loading API"))
        }) {
            state.postValue(ApiResponse.loading(null))
            repository.getGitRepos(page).let {
                state.postValue(ApiResponse.success(it))
            }
        }
    }
}