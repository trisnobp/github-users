package com.dicoding.githubuser.data.local.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubuser.data.local.entity.FavoriteUser
import com.dicoding.githubuser.data.local.room.FavoriteUserDao
import com.dicoding.githubuser.data.local.room.FavoriteUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        mUserDao = db.favoriteUserDao()
    }

    fun insert(user: FavoriteUser) {
        executorService.execute {
            mUserDao.insert(user)
        }
    }

    fun delete(username: String) {
        executorService.execute {
            mUserDao.delete(username)
        }
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = mUserDao.getAllFavoriteUser()

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> = mUserDao.getFavoriteUserByUsername(username)
}