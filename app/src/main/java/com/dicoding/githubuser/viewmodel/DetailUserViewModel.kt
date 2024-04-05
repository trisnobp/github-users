package com.dicoding.githubuser.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.local.entity.FavoriteUser
import com.dicoding.githubuser.data.local.repository.FavoriteUserRepository
import com.dicoding.githubuser.data.remote.response.DetailUserResponse
import com.dicoding.githubuser.data.remote.response.ItemsItem
import com.dicoding.githubuser.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class DetailUserViewModel(application: Application): ViewModel() {

    private val mFavoriteRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    private val _userDetailData = MutableLiveData<DetailUserResponse>()
    val userDetailData: LiveData<DetailUserResponse> = _userDetailData

    private val _followingCount = MutableLiveData<Int>()
    val followingCount: LiveData<Int> = _followingCount

    private val _followersCount = MutableLiveData<Int>()
    val followersCount: LiveData<Int> = _followersCount

    private val _isDetailLoading = MutableLiveData<Boolean>()
    val isDetailLoading: LiveData<Boolean> = _isDetailLoading

    fun insert(user: FavoriteUser) {
        mFavoriteRepository.insert(user)
    }

    fun delete(username: String) {
        mFavoriteRepository.delete(username)
    }

    fun getFavoriteUser(username: String): LiveData<FavoriteUser> = mFavoriteRepository.getFavoriteUserByUsername(username)

    fun getUserDetail(username: String) {
        getFollowersCount(username)
        getFollowingCount(username)
        _isDetailLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object: retrofit2.Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isDetailLoading.value = false
                if (response.isSuccessful) {
                    _userDetailData.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isDetailLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }


    private fun getFollowingCount(username: String) {
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object: retrofit2.Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _followingCount.value = response.body()?.size
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isDetailLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun getFollowersCount(username: String) {
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object: retrofit2.Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful) {
                    _followersCount.value = response.body()?.size
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isDetailLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "DetailUserViewModel"
    }

}