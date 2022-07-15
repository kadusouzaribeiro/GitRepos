package br.com.android.gitrepos.data.di

import android.util.Log
import br.com.android.gitrepos.data.repository.GitRepository
import br.com.android.gitrepos.data.repository.GitRepositoryImpl
import br.com.android.gitrepos.data.services.GitApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by Carlos Souza on 15,julho,2022
 */
object DataModule {

    private const val BASE_URL = "https://api.github.com/"
    private const val OK_HTTP = "Retrofit"

    private fun gitRepositoryModule() : Module {
        return module {
            single<GitRepository> {GitRepositoryImpl(get())}
        }
    }

    fun load() {
        loadKoinModules(gitRepositoryModule() + networkModule())
    }

    private fun networkModule() : Module {
        return module {
            single<GitApi> { createService(get(), get()) }

            single {
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            }

            single {
                val interceptor = HttpLoggingInterceptor {
                    Log.d(OK_HTTP, it )
                }

                interceptor.level = HttpLoggingInterceptor.Level.BODY

                OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            }
        }
    }

    private inline fun <reified T> createService(
        factory: Moshi,
        client: OkHttpClient
    ) : T {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(factory))
            .client(client)
            .build()
            .create(T::class.java)
    }
}