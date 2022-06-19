package br.com.android.gitrepos

import br.com.android.gitrepos.di.apiModule
import br.com.android.gitrepos.di.networkModule
import br.com.android.gitrepos.di.repositoryModule
import br.com.android.gitrepos.di.viewModelModule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

/**
 * Created by Carlos Souza on 17,junho,2022
 */
class CheckModulesTest : KoinTest {

    @Test
    fun checkAllModules() = checkModules {
        modules(
            networkModule,
            apiModule,
            repositoryModule,
            viewModelModule
        )
    }
}