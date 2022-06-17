package br.com.android.gitrepos

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
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
import br.com.android.gitrepos.utils.Permissions
import br.com.android.gitrepos.viewmodel.GitViewModel
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private val viewModel: GitViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding
    private lateinit var reposAdapter: GitRepoAdapter

    private var permissionDenied = false
    private val listRepos: MutableList<Item> = mutableListOf()
    private var loading = false
    private var initialSize = 0
    private var page = 1

    private val RECYCLER_STATE = "recycler_state"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (savedInstanceState != null) {
            binding.rvRepoInfo.layoutManager?.onRestoreInstanceState(savedInstanceState.getParcelable(RECYCLER_STATE))
        }
        setObservable()
        getData()
//        if (Permissions.hasInternetPermission(this)) {
//            getData()
//        } else {
//            Permissions.requestInternetPermission(this)
//        }
    }

    private fun getData() {
        val gson = Gson()
        val cachedRepos = RepoCache.read(applicationContext)
        val cachedPage = RepoCache.read(applicationContext, true)
        if (cachedRepos != "" && cachedPage != "") {
            page = cachedPage.toInt()
            val gd = gson.fromJson(cachedRepos, GitData::class.java)
            listRepos.addAll(gd.items)
            setReposList(listRepos)
        } else {
            viewModel.getRepos(page)
        }
    }

    private fun setObservable() {
        viewModel.state.observe(this) {
            when (it.status) {
                ResponseStatus.SUCCESS -> {
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
                        RepoCache.save(applicationContext, page.toString(), true)
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
                        loading = true
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == Permissions.INTERNET_PERMISSION_CODE &&
                grantResults[0] == PackageManager.PERMISSION_DENIED && !permissionDenied) {
            permissionDenied = true
            Permissions.requestInternetPermission(this)
        } else {
            getData()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(RECYCLER_STATE, binding.rvRepoInfo.layoutManager?.onSaveInstanceState())

    }
}