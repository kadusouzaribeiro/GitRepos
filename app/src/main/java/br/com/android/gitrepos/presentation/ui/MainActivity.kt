package br.com.android.gitrepos.presentation.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.android.gitrepos.R
import br.com.android.gitrepos.data.ResponseStatus
import br.com.android.gitrepos.data.model.Item
import br.com.android.gitrepos.databinding.ActivityMainBinding
import br.com.android.gitrepos.presentation.adapter.GitRepoAdapter
import br.com.android.gitrepos.presentation.ui.home.GitViewModel
import br.com.android.gitrepos.utils.CacheData
import br.com.android.gitrepos.utils.Permissions
import br.com.android.gitrepos.utils.RepoCache
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}