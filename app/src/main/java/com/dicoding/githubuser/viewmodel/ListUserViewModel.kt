package com.dicoding.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.remote.response.GithubResponse
import com.dicoding.githubuser.data.remote.response.ItemsItem
import com.dicoding.githubuser.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class ListUserViewModel: ViewModel() {

    private val _users = MutableLiveData<List<ItemsItem?>?>()
    val users: LiveData<List<ItemsItem?>?> = _users

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFound = MutableLiveData<Boolean>()
    val isFound: LiveData<Boolean> = _isFound

    companion object{
        private const val TAG = "ListUserViewModel"
        private const val DEFAULT_USERNAME = "Arif"
    }

    init {
        searchUser(DEFAULT_USERNAME)
    }

   fun searchUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchAccount(username)
        client.enqueue(object : retrofit2.Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isFound.value = response.body()?.totalCount != 0
                    _users.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }


}