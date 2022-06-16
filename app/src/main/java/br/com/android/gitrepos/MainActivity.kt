package br.com.android.gitrepos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.android.gitrepos.data.ResponseStatus
import br.com.android.gitrepos.databinding.ActivityMainBinding
import br.com.android.gitrepos.viewmodel.GitViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: GitViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setObservable()

        viewModel.getRepos(1)
    }

    private fun setObservable() {
        viewModel.state.observe(this) {
            when (it.status) {
                ResponseStatus.SUCCESS -> {
                    val repos = it.data
                    repos?.let { g ->
                        g.items.forEach { i ->
                            Log.d("GitRepos", "Nome: ${i.name}")
                        }
                    }
                }
                ResponseStatus.LOADING -> {
                    binding.txtMain.text = "Loading"
                }
                ResponseStatus.ERROR -> {
                    it.message?.let { it1 -> binding.txtMain.text = "Erro: $it1" }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.state.removeObservers(this)
    }

    //montar o layout dos itens
    //montar o recyclerview
    //montar o adapter
    //vincular os dados ao adapter
    //decidir maneira de fazer o cache (Arquivo ou Room)
    //fazer o cache
    //acertar a orientação do aparelho, savedinstancestate
}