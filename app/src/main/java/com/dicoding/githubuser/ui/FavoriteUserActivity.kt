package com.dicoding.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.remote.response.ItemsItem
import com.dicoding.githubuser.databinding.ActivityFavoriteUserBinding
import com.dicoding.githubuser.viewmodel.FavoriteViewModel
import com.dicoding.githubuser.viewmodel.UserViewModelFactory

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding

    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavoriteUsers.addItemDecoration(itemDecoration)

        supportActionBar?.title = resources.getString(R.string.second_bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        favoriteViewModel = obtainViewModel(this@FavoriteUserActivity)

        favoriteViewModel.getAllFavoriteUser().observe(this) { users ->
            val items = arrayListOf<ItemsItem>()

            if (users.isEmpty()) {
                binding.tvDescription.visibility = View.VISIBLE
                binding.tvDescription.text = resources.getString(R.string.no_favorite)
                binding.rvFavoriteUsers.visibility = View.GONE
            } else {
                users.map {
                    val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
                    items.add(item)

                    val adapter = FavoriteUserAdapter()
                    adapter.submitList(items)
                    binding.rvFavoriteUsers.adapter = adapter
                }
                binding.tvDescription.visibility = View.GONE
                binding.rvFavoriteUsers.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = UserViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }
}