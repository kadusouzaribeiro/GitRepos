package br.com.android.gitrepos.core

/**
 * Created by Carlos Souza on 15,julho,2022
 */
sealed class State<out T: Any> {

    object Loading: State<Nothing>()

    data class Success<out T: Any>(val result: T) : State<T>()

    data class Error(val error: Throwable) : State<Nothing>()

}
