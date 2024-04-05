package com.dicoding.githubuser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.data.remote.response.ItemsItem
import com.dicoding.githubuser.databinding.FragmentFollowBinding
import com.dicoding.githubuser.viewmodel.DetailUserViewModel
import com.dicoding.githubuser.viewmodel.FollowViewModel
import com.dicoding.githubuser.viewmodel.UserViewModelFactory

class FollowFragment : Fragment() {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private lateinit var detailUserViewModel: DetailUserViewModel
    private val followViewModel by viewModels<FollowViewModel>()

    private var position: Int = 0
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailUserViewModel = obtainViewModel()

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollowUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollowUsers.addItemDecoration(itemDecoration)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        followViewModel.isFollowLoading.observe(requireActivity()) {
            showLoading(it)
        }

        followViewModel.followersData.observe(requireActivity()) {
            setListFollowerData(it)
        }

        followViewModel.followingData.observe(requireActivity()) {
            setListFollowingData(it)
        }


        if (position == 1) {
            followViewModel.getUserFollowers(username)
        } else {
            followViewModel.getUserFollowing(username)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setListFollowerData(users: List<ItemsItem?>?) {
        if (users!!.isEmpty()) {
            binding.rvFollowUsers.visibility = View.GONE
            binding.tvFollowersDescription.visibility = View.VISIBLE
        } else {
            val adapter = FollowUserAdapter()
            adapter.submitList(users)
            binding.rvFollowUsers.adapter = adapter
        }
    }

    private fun setListFollowingData(users: List<ItemsItem?>?) {
        if (users!!.isEmpty()) {
            binding.rvFollowUsers.visibility = View.GONE
            binding.tvFollowingDescription.visibility = View.VISIBLE
        } else {
            val adapter = FollowUserAdapter()
            adapter.submitList(users)
            binding.rvFollowUsers.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun obtainViewModel(): DetailUserViewModel {
        val factory = UserViewModelFactory.getInstance(requireActivity().application)
        return ViewModelProvider(requireActivity(), factory).get(DetailUserViewModel::class.java)
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}