package com.dicoding.githubuser.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.data.remote.response.ItemsItem
import com.dicoding.githubuser.databinding.ItemUserBinding

class FavoriteUserAdapter: androidx.recyclerview.widget.ListAdapter<ItemsItem, FavoriteUserAdapter.MyViewHolder>(
    UserAdapter.DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)

        holder.itemView.setOnClickListener { _ ->
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra("username", review.login)
            holder.itemView.context.startActivity(intentDetail)
        }
    }
    class MyViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ItemsItem){
            Glide.with(itemView)
                .load(review.avatarUrl)
                .into(binding.imgItemPhoto)

            binding.usernameSearch.text = review.login.toString()
        }
    }
}