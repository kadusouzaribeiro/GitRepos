package br.com.android.gitrepos.di

import android.util.Log
import br.com.android.gitrepos.data.remote.GitApi
import br.com.android.gitrepos.data.remote.GitRepository
import br.com.android.gitrepos.viewmodel.GitViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Carlos Souza on 15,junho,2022
 */

val networkModule = module {

    val baseUrl = "https://api.github.com/"

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun getClient(): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        val interceptor =
            HttpLoggingInterceptor { message ->
                Log.d("Retrofit", message)
            }
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return clientBuilder.addInterceptor(interceptor).build()
    }

    fun retrofit(client: OkHttpClient, baseURL: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .baseUrl(baseURL)
            .build()
    }

    single { getClient() }
    single { retrofit(get(), baseUrl) }

}

val apiModule = module {
    fun provideApi(retrofit: Retrofit): GitApi {
        return retrofit.create(GitApi::class.java)
    }
    single { provideApi(get()) }
}

val repositoryModule = module {
    single { GitRepository(get()) }
}

val viewModelModule = module {
    viewModel { GitViewModel(repository = get()) }
}