package br.com.android.gitrepos.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.android.gitrepos.core.State
import br.com.android.gitrepos.data.model.GitData
import br.com.android.gitrepos.data.repository.GitRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

/**
 * Created by Carlos Souza on 15,junho,2022
 */
class GitViewModel(private val repository: GitRepository) : ViewModel() {

    private val _state = MutableLiveData<State<GitData>>()
    val state : LiveData<State<GitData>>
        get() = _state

    fun getReposList(page: Int) {
        getRepos(page)
    }

    private fun getRepos(page: Int = 1) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _state.postValue(State.Error(throwable))
        }) {
            _state.postValue(State.Loading)
            repository.getGitRepos(page).let {
                _state.postValue(State.Success(it))
            }
        }
    }
}