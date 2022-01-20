package com.dicoding.githubapi.api

import com.google.gson.annotations.SerializedName

data class FavoriteRespone(

    @field:SerializedName("items")
    val items: ArrayList<UserFavorite>
)

data class UserFavorite(
    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("avatar_url")
    val avatar_Url: String,

    @field:SerializedName("html_url")
    val html_Url: String
)