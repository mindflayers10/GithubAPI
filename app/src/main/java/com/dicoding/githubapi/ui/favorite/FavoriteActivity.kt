package com.dicoding.githubapi.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubapi.api.UserFavorite
import com.dicoding.githubapi.databinding.ActivityFavoriteBinding
import com.dicoding.githubapi.db.Favorite
import com.dicoding.githubapi.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter
    private lateinit var viewModel: FavoriteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Favorite User"


        adapter = FavoriteAdapter()
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserFavorite) {
                Intent(this@FavoriteActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailActivity.EXTRA_URL, data.avatar_Url)
                    it.putExtra(DetailActivity.EXTRA_HTML, data.html_Url)
                    startActivity(it)
                }
            }

        })

        binding.apply {
            rvUser.setHasFixedSize(true)
            rvUser.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvUser.adapter = adapter
        }

        viewModel.getFavoriteUser()?.observe(this, {
            if (it != null) {
                val list = mapList(it)
                adapter.setList(list)
            }
        })


    }

    private fun mapList(users: List<Favorite>): ArrayList<UserFavorite> {
        val listUsers = ArrayList<UserFavorite>()
        for (user in users) {
            val userMapped = UserFavorite(
                user.login,
                user.id,
                user.avatar_url,
                user.html_url
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }
}