package com.dicoding.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.remote.response.ItemsItem
import com.dicoding.githubuser.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class FollowViewModel: ViewModel() {

    private val _followingData = MutableLiveData<List<ItemsItem?>?>()
    val followingData: LiveData<List<ItemsItem?>?> = _followingData

    private val _followersData = MutableLiveData<List<ItemsItem?>?>()
    val followersData: LiveData<List<ItemsItem?>?> = _followersData

    private val _isFollowLoading = MutableLiveData<Boolean>()
    val isFollowLoading: LiveData<Boolean> = _isFollowLoading

    fun getUserFollowers(username: String) {
        _isFollowLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object: retrofit2.Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isFollowLoading.value = false
                if (response.isSuccessful) {
                    _followersData.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isFollowLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUserFollowing(username: String) {
        _isFollowLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object: retrofit2.Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isFollowLoading.value = false
                if (response.isSuccessful) {
                    _followingData.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isFollowLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "FollowViewModel"
    }
}