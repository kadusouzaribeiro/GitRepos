package br.com.android.gitrepos

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.android.gitrepos.adapter.GitRepoAdapter
import br.com.android.gitrepos.data.ResponseStatus
import br.com.android.gitrepos.data.remote.dto.GitData
import br.com.android.gitrepos.data.remote.dto.Item
import br.com.android.gitrepos.databinding.ActivityMainBinding
import br.com.android.gitrepos.viewmodel.GitViewModel
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: GitViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding
    private lateinit var reposAdapter: GitRepoAdapter

    private val listRepos: MutableList<Item> = mutableListOf()
    private var loading = false
    private var initialSize = 0
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val gson = Gson()

        val cachedRepos = RepoCache.read(applicationContext)

        val gd = gson.fromJson(cachedRepos, GitData::class.java)

        setObservable()

        viewModel.getRepos(page)
    }

    private fun setObservable() {
        viewModel.state.observe(this) {
            when (it.status) {
                ResponseStatus.SUCCESS -> {
                    Log.d("GitRepos", "listrepos: ${listRepos.size}")
                    val repos = it.data
                    repos?.let { g ->
                        if (listRepos.size == 0) {
                            listRepos.addAll(g.items)
                            setReposList(listRepos)
                        } else {
                            listRepos.addAll(g.items)
                            reposAdapter.notifyItemRangeInserted(initialSize, listRepos.size)
                        }

                        val gson = Gson()

                        val gd = GitData(
                            items = listRepos
                        )

                        RepoCache.save(applicationContext, gson.toJson(gd))
                        binding.pbGitrepos.visibility = View.GONE
                        loading = false
                    }
                }
                ResponseStatus.LOADING -> {
                    loading = true
                    if (listRepos.size == 0) {
                        binding.pbGitrepos.visibility = View.VISIBLE
                    }
                }
                ResponseStatus.ERROR -> {
                    binding.pbGitrepos.visibility = View.GONE
                    it.message?.let { it1 -> showMessage(it1) }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.state.removeObservers(this)
    }

    private fun setReposList(list: List<Item>) {
        binding.apply {
            reposAdapter = GitRepoAdapter(list)
            rvRepoInfo.adapter = reposAdapter

            val lManager = rvRepoInfo.layoutManager as LinearLayoutManager

            rvRepoInfo.addOnScrollListener(object: RecyclerView.OnScrollListener(){
                var visibleItemCount = 0
                var totalItemCount = 0
                var firstVisibleItem = 0
                var lastCompletelyVisibleItem = 0

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    visibleItemCount = rvRepoInfo.childCount
                    totalItemCount = lManager.itemCount
                    firstVisibleItem = lManager.findFirstVisibleItemPosition()
                    lastCompletelyVisibleItem = lManager.findLastVisibleItemPosition()

                    if (!loading && (firstVisibleItem + (visibleItemCount*2)) >= totalItemCount - 1) {
                        initialSize = listRepos.size
                        page += 1
                        viewModel.getRepos(page)
                    }

                }
            })
        }
    }

    private fun showMessage(msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Git Repos")
        builder.setMessage(msg)
        builder.setNeutralButton("Ok") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    //decidir maneira de fazer o cache (Arquivo ou Room)
    //fazer o cache
    //acertar a orientação do aparelho, savedinstancestate
}