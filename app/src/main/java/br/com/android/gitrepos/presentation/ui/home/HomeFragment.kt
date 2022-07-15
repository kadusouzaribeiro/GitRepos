package br.com.android.gitrepos.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.android.gitrepos.R
import br.com.android.gitrepos.core.State
import br.com.android.gitrepos.data.model.Item
import br.com.android.gitrepos.databinding.FragmentHomeBinding
import br.com.android.gitrepos.presentation.adapter.GitRepoAdapter
import br.com.android.gitrepos.utils.CacheData
import br.com.android.gitrepos.utils.RepoCache
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel: GitViewModel by viewModel()

    private lateinit var binding: FragmentHomeBinding
    private lateinit var reposAdapter: GitRepoAdapter

    private val listRepos: MutableList<Item> = mutableListOf()
    private var loading = false
    private var initialSize = 0
    private var page = 1

    private val RECYCLER_STATE = "recycler_state"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false).also {
        binding = FragmentHomeBinding.bind(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            binding.rvRepoInfo.layoutManager?.onRestoreInstanceState(savedInstanceState.getParcelable(RECYCLER_STATE))
        }
        setObservable()
        getData()
    }

    private fun getData() {
        val gson = Gson()
        val cachedRepos = RepoCache.read(requireContext())
        if (cachedRepos != "") {
            val cd = gson.fromJson(cachedRepos, CacheData::class.java)
            page = cd.page
            listRepos.addAll(cd.items)
            setReposList(listRepos)
        } else {
            viewModel.getReposList(page)
        }
    }

    private fun setObservable() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is State.Loading -> {
                    loading = true
                    if (listRepos.size == 0) {
                        binding.pbGitrepos.visibility = View.VISIBLE
                    }
                }
                is State.Success -> {
                    val repos = it.result
                    repos.let { g ->
                        if (listRepos.size == 0) {
                            listRepos.addAll(g.items)
                            setReposList(listRepos)
                        } else {
                            listRepos.addAll(g.items)
                            reposAdapter.notifyItemRangeInserted(initialSize, listRepos.size)
                        }

                        val gson = Gson()
                        val cacheData = CacheData(
                            page = page,
                            items = listRepos
                        )
                        RepoCache.save(requireContext(), gson.toJson(cacheData))
                        binding.pbGitrepos.visibility = View.GONE
                        loading = false
                    }
                }
                is State.Error -> {
                    binding.pbGitrepos.visibility = View.GONE
                    showMessage(it.error.message ?: "Error Loading API")
                    it.error.message?.let { m -> showMessage(m) }
                }
            }
        }
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
                        viewModel.getReposList(page)
                    }

                }
            })
        }
    }

    private fun showMessage(msg: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Git Repos")
        builder.setMessage(msg)
        builder.setNeutralButton("Ok") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.state.removeObservers(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(RECYCLER_STATE, binding.rvRepoInfo.layoutManager?.onSaveInstanceState())
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}