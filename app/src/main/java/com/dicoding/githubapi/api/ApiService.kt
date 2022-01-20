package com.dicoding.githubapi.api

import com.dicoding.githubapi.User
import com.dicoding.githubapi.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetail(
        @Path("username") username: String
    ): Call<User>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollower(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>
}
