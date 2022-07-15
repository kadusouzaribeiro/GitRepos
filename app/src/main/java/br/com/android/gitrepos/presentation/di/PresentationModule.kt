package br.com.android.gitrepos.presentation.di

import br.com.android.gitrepos.presentation.ui.home.GitViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Created by Carlos Souza on 15,julho,2022
 */
object PresentationModule {

    fun load() {
        loadKoinModules(viewModelModule())
    }

    private fun viewModelModule() : Module {
        return module {
            viewModel { GitViewModel(get()) }
        }
    }
}