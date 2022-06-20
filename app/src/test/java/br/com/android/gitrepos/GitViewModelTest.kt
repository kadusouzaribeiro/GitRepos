package br.com.android.gitrepos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.android.gitrepos.data.ApiResponse
import br.com.android.gitrepos.data.remote.GitRepository
import br.com.android.gitrepos.data.remote.dto.GitData
import br.com.android.gitrepos.data.remote.dto.Item
import br.com.android.gitrepos.data.remote.dto.Owner
import br.com.android.gitrepos.viewmodel.GitViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class GitViewModelTest {
    private lateinit var viewModel: GitViewModel
    private lateinit var gitRepository: GitRepository
    private lateinit var stateObserver: Observer<ApiResponse<GitData?>>

    private val successData = returnRepos()

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI Thread")

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        gitRepository = mock()
        runBlocking {
            whenever(gitRepository.getGitRepos(1)).thenReturn(successData)
            whenever(gitRepository.getGitRepos(0)).thenReturn(null)
        }
        viewModel = GitViewModel(gitRepository)
        stateObserver = mock()
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `when getRepos is called with valid page, then observer is updated with success`() = runBlocking {
        viewModel.state.observeForever(stateObserver)
        viewModel.getRepos(1)
        delay(10)
        verify(gitRepository).getGitRepos(1)
        verify(stateObserver, timeout(50)).onChanged(ApiResponse.loading(null))
        verify(stateObserver, timeout(50)).onChanged(ApiResponse.success(successData))
    }

    private fun returnRepos(): GitData {
        val owner = Owner(
            id = 82592,
            avatar_url = "https://avatars.githubusercontent.com/u/82592?v=4",
            login = "square"
        )

        val item = Item(
            id = 5152285,
            forks = 8865,
            full_name = "square/okhttp",
            name = "okhttp",
            stargazers_count = 42330,
            owner = owner
        )

        val list = arrayListOf<Item>()

        list.add(item)

        return GitData(
            items = list
        )
    }
}