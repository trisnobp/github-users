package com.dicoding.githubuser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.data.remote.response.ItemsItem
import com.dicoding.githubuser.databinding.FragmentListUserBinding
import com.dicoding.githubuser.viewmodel.ListUserViewModel


class ListUserFragment : Fragment() {

    private var _binding: FragmentListUserBinding? = null
    private val binding get() = _binding!!
    private val listUserViewModel by viewModels<ListUserViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListUserBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        listUserViewModel.users.observe(requireActivity()) { users ->
            setListUserData(users)
        }

        listUserViewModel.isLoading.observe(requireActivity()) {
            showLoading(it)
        }

        listUserViewModel.isFound.observe(requireActivity()) {
            showNotFoundMessage(it)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    // Set the RecyclerView Item
                    listUserViewModel.searchUser(searchBar.text.toString())
                    false
                }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setListUserData(users: List<ItemsItem?>?) {
        val adapter = UserAdapter()
        adapter.submitList(users)
        binding.rvUsers.adapter = adapter
    }

    private fun showNotFoundMessage(isFound: Boolean) {
        binding.tvDescription.visibility = if (isFound) View.GONE else View.VISIBLE
        binding.rvUsers.visibility = if (isFound) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}