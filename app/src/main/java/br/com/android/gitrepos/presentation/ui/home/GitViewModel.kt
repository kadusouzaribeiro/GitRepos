package br.com.android.gitrepos.presentation.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.android.gitrepos.data.ApiResponse
import br.com.android.gitrepos.data.model.GitData
import br.com.android.gitrepos.data.repository.GitRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * Created by Carlos Souza on 15,junho,2022
 */
class GitViewModel(private val repository: GitRepository) : ViewModel() {

    val state = MutableLiveData<ApiResponse<GitData?>>()

    fun getRepos(page: Int = 1) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            state.postValue(ApiResponse.error(null, throwable.message ?: "Error loading API"))
        }) {
            state.postValue(ApiResponse.loading(null))
            repository.getGitRepos().let {
                state.postValue(ApiResponse.success(it))
            }
        }
    }
}