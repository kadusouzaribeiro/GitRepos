package br.com.android.gitrepos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.android.gitrepos.GlideApp
import br.com.android.gitrepos.R
import br.com.android.gitrepos.data.remote.dto.Item
import br.com.android.gitrepos.databinding.ItemRepoBinding

/**
 * Created by Carlos Souza on 16,junho,2022
 */
class GitRepoAdapter(private val listRepos: List<Item>): RecyclerView.Adapter<GitRepoAdapter.ReposViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposViewHolder {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReposViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        listRepos[position].let {
            with(holder) {
                binding.apply {
                    tvName.text = it.name
                    tvFullname.text = it.full_name
                    GlideApp.with(itemView)
                        .load(it.owner.avatar_url)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .fallback(R.drawable.placeholder)
                        .into(ivAvatar)
                    tvLogin.text = it.owner.login
                    tvFork.text = String.format("Fork %s", it.forks)
                    tvStar.text = String.format("Star %s", it.stargazers_count)
                }
            }
        }
    }

    override fun getItemCount() = listRepos.size

    inner class ReposViewHolder(val binding: ItemRepoBinding): RecyclerView.ViewHolder(binding.root)
}