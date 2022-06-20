package br.com.android.gitrepos.data.remote.dto

import java.io.Serializable

/**
 * Created by Carlos Souza on 15,junho,2022
 */
data class GitData(
    val items: List<Item>
)

data class Item(
    val id: Int,
    val forks: Int,
    val full_name: String,
    val name: String,
    val stargazers_count: Int,
    val owner: Owner
): Serializable

data class Owner(
    val id: Int,
    val avatar_url: String,
    val login: String
)