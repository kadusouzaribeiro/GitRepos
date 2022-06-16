package br.com.android.gitrepos

import android.app.Application
import br.com.android.gitrepos.di.apiModule
import br.com.android.gitrepos.di.networkModule
import br.com.android.gitrepos.di.repositoryModule
import br.com.android.gitrepos.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Created by Carlos Souza on 15,junho,2022
 */
class GitApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@GitApplication)
            modules(
                networkModule,
                apiModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}