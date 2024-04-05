package com.dicoding.githubuser.data.remote.retrofit

import com.dicoding.githubuser.data.remote.response.DetailUserResponse
import com.dicoding.githubuser.data.remote.response.GithubResponse
import com.dicoding.githubuser.data.remote.response.ItemsItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun searchAccount(
        @Query("q") query: String
    ): retrofit2.Call<GithubResponse>

    @GET("/users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): retrofit2.Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String):
            retrofit2.Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String):
            retrofit2.Call<List<ItemsItem>>
}