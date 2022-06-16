package br.com.android.gitrepos.data

/**
 * Created by Carlos Souza on 15,junho,2022
 */
data class ApiResponse<out T>(val status: ResponseStatus, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> =
            ApiResponse(status = ResponseStatus.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): ApiResponse<T> =
            ApiResponse(status = ResponseStatus.ERROR, data = null, message = message)

        fun <T> loading(data: T?): ApiResponse<T> =
            ApiResponse(status = ResponseStatus.LOADING, data = null, message = null)
    }
}

enum class ResponseStatus { SUCCESS, ERROR, LOADING }
