package com.dicoding.githubapi.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.githubapi.db.Favorite
import com.dicoding.githubapi.db.FavoriteDao
import com.dicoding.githubapi.db.FavoriteDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var userDao: FavoriteDao?
    private var userDb: FavoriteDatabase?

    init {
        userDb = FavoriteDatabase.getDatabase(application)
        userDao = userDb?.favoriteDao()
    }

    fun getFavoriteUser(): LiveData<List<Favorite>>? {
        return userDao?.getFavorite()
    }

}