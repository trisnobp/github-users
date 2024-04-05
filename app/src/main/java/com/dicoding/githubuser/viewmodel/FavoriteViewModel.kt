package com.dicoding.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.data.local.entity.FavoriteUser
import com.dicoding.githubuser.data.local.repository.FavoriteUserRepository

class FavoriteViewModel(application: Application): ViewModel() {
    private val mFavoriteRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> {
       return mFavoriteRepository.getAllFavoriteUser()
    }
}