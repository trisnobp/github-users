package com.dicoding.githubuser.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.local.entity.FavoriteUser
import com.dicoding.githubuser.data.remote.response.DetailUserResponse
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.viewmodel.DetailUserViewModel
import com.dicoding.githubuser.viewmodel.UserViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {

    private lateinit var detailUsername: String
    private lateinit var binding: ActivityDetailBinding
    private var isFavorite = false

    private lateinit var detailUserViewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        detailUsername = if (intent.getStringExtra("username") == null) {
            DetailActivityArgs.fromBundle(intent.extras!!).username
        } else {
            intent.getStringExtra("username")!!
        }

        detailUserViewModel = obtainViewModel(this@DetailActivity)

        detailUserViewModel.isDetailLoading.observe(this) {
            showLoading(it)
        }

        detailUserViewModel.getFavoriteUser(detailUsername).observe(this) { user ->
            if (user == null) {
                binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(binding.fabFavorite.context, R.drawable.baseline_favorite_border_24))
            } else {
                isFavorite = true
                binding.fabFavorite.setImageDrawable(ContextCompat.getDrawable(binding.fabFavorite.context, R.drawable.baseline_favorite_24))
            }
        }

        detailUserViewModel.userDetailData.observe(this) {
            setUserData(it)
        }

        detailUserViewModel.followersCount.observe(this) {
            setFollowersData(it)
        }

        detailUserViewModel.followingCount.observe(this) {
            setFollowingData(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.setUsername(detailUsername)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        detailUserViewModel.getUserDetail(detailUsername)

        binding.fabFavorite.setOnClickListener {
            isFavorite = if (isFavorite) {
                detailUserViewModel.delete(detailUsername)
                false
            } else {
                val newUser = FavoriteUser(detailUsername, detailUserViewModel.userDetailData.value?.avatarUrl)
                detailUserViewModel.insert(newUser)
                true
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.imgItemPhoto.visibility = View.GONE
            binding.detailName.visibility = View.GONE
            binding.detailUsername.visibility = View.GONE
            binding.followers.visibility = View.GONE
            binding.following.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.imgItemPhoto.visibility = View.VISIBLE
            binding.detailName.visibility = View.VISIBLE
            binding.detailUsername.visibility = View.VISIBLE
            binding.followers.visibility = View.VISIBLE
            binding.following.visibility = View.VISIBLE
        }
    }

    private fun setUserData(data: DetailUserResponse) {
        Glide.with(this)
            .load(data.avatarUrl)
            .into(binding.imgItemPhoto)

        binding.detailName.text = data.login
        binding.detailUsername.text = data.name
    }

    private fun setFollowersData(data: Int) {
        binding.followers.text = String.format(resources.getString(R.string.followers_example), data)
    }

    private fun setFollowingData(data: Int) {
        binding.following.text = String.format(resources.getString(R.string.following_example), data)
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val factory = UserViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailUserViewModel::class.java)
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}