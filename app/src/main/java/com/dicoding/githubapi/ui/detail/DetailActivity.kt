package com.dicoding.githubapi.ui.detail

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.dicoding.githubapi.R
import com.dicoding.githubapi.databinding.ActivityDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)


        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl: String = intent.getStringExtra(EXTRA_URL).toString()
        val htmlUrl = intent.getStringExtra(EXTRA_HTML)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        if (username != null) {
            viewModel.setUserDetail(username)
        } else {
            Log.d(ContentValues.TAG, "onFailure")
        }

        viewModel.getUserDetail().observe(this, {
            if (it != null) {
                binding.apply {
                    tvDetailName.text = it.name
                    tvDetailUsername.text = it.login
                    tvDetailCompany.text = it.company
                    tvDetailLocation.text = it.location
                    tvDetailFollower.text = it.followers.toString()
                    tvDetailFollowing.text = it.following.toString()
                    tvRepo.text = getString(R.string.repository, it.publicRepos.toString())
                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .transform(CircleCrop())
                        .into(imgAvatar)
                }
            }
        })
        //cek apakah sudah difavoritkan atau belum
        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.toggleFavorite.isChecked = true
                        _isChecked = true
                    } else {
                        binding.toggleFavorite.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        // untuk meremove favorite
        binding.toggleFavorite.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                if (htmlUrl != null) {
                    if (username != null) {
                        viewModel.addFavorite(username, id, avatarUrl, htmlUrl)
                    }
                }
            } else {
                viewModel.removeFavorite(id)
            }
            binding.toggleFavorite.isChecked = _isChecked
        }

        val sectionsPagerAdapter = username?.let {
            SectionsPagerAdapter(this@DetailActivity, it)
        }
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TITLE_TAB[position])
        }.attach()

        viewModel.isLoading.observe(this, {
            showLoading(it)
        })
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_HTML = "extra_html"

        @StringRes
        private val TITLE_TAB = intArrayOf(R.string.follower, R.string.following)
    }
}