package com.dicoding.githubapi.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel


import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.githubapi.User
import com.dicoding.githubapi.api.ApiConfig
import com.dicoding.githubapi.db.Favorite
import com.dicoding.githubapi.db.FavoriteDao
import com.dicoding.githubapi.db.FavoriteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application){
    val user = MutableLiveData<User>()
    fun getUserDetail(): LiveData<User> {
        return user
    }

    private var userDao: FavoriteDao?
    private var userDb : FavoriteDatabase?

    init {
        userDb = FavoriteDatabase.getDatabase(application)
        userDao = userDb?.favoriteDao()
    }


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetail(username)
        client.enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    user.postValue(response.body())
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }


    fun addFavorite(username: String, id: Int, avatarUrl: String, html_url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = Favorite(username, id, avatarUrl, html_url)
            userDao?.addFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFavorite(id)
        }
    }
}