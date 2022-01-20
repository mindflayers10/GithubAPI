package com.dicoding.githubapi.ui.main

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubapi.R
import com.dicoding.githubapi.User
import com.dicoding.githubapi.databinding.ActivityMainBinding
import com.dicoding.githubapi.theme.SettingPreferences
import com.dicoding.githubapi.theme.ThemeViewModel
import com.dicoding.githubapi.theme.ViewModelFactory
import com.dicoding.githubapi.ui.detail.DetailActivity
import com.dicoding.githubapi.ui.favorite.FavoriteActivity
import com.google.android.material.switchmaterial.SwitchMaterial

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsersAdapter
    private val viewModel by viewModels<MainViewModel>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter = UsersAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : UsersAdapter.OnItemClickCallback {

            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_URL, data.avatarUrl)
                    it.putExtra(DetailActivity.EXTRA_HTML, data.htmlUrl)
                    startActivity(it)
                }
            }
        })

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = adapter
        binding.rvUsers.setHasFixedSize(true)


        viewModel.getListUsers().observe(this, {
            if (it != null) {
                adapter.setList(it)
            }
        })

        viewModel.isLoading.observe(this, {
            showLoading(it)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.findUsers(query)
                return true
            }


            override fun onQueryTextChange(query: String): Boolean {
                viewModel.findUsers(query)
                return false
            }
        })


        val item: MenuItem = menu.findItem(R.id.dark_mode)
        item.setActionView(R.layout.switch_dark_mode)

        val switch: SwitchMaterial =
            menu.findItem(R.id.dark_mode).actionView.findViewById(R.id.switch_theme)
        val pref = SettingPreferences.getInstance(dataStore)
        val themeViewModel =
            ViewModelProvider(this, ViewModelFactory(pref)).get(ThemeViewModel::class.java)

        themeViewModel.getThemeSettings().observe(this, { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switch.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switch.isChecked = false
            }
        })

        switch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            themeViewModel.saveThemeSetting(isChecked)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_menu -> {
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}