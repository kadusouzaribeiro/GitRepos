package br.com.android.gitrepos.utils

import br.com.android.gitrepos.data.model.Item

/**
 * Created by Carlos Souza on 17,junho,2022
 */
data class CacheData (
    val page: Int,
    val items: List<Item>
)