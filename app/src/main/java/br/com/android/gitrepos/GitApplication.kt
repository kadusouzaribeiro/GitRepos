package br.com.android.gitrepos

import android.app.Application
import br.com.android.gitrepos.data.di.DataModule
import br.com.android.gitrepos.presentation.di.PresentationModule
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
        }

        DataModule.load()
        PresentationModule.load()
    }
}