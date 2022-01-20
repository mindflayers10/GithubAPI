package com.dicoding.githubapi.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_user")
data class Favorite(
    val login: String,

    @PrimaryKey
    val id: Int,
    val avatar_url: String,
    val html_url: String

) : Serializable

