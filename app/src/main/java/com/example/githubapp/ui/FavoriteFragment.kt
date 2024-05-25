package com.example.githubapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.databinding.FragmentFavoriteBinding
import com.example.githubapp.ui.adapter.RecyclerViewAdapter
import com.example.githubapp.ui.viewmodel.FavoriteViewModel
import com.example.githubapp.ui.viewmodel.FavoriteViewModelFactory

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewmodel: FavoriteViewModel by viewModels {
        FavoriteViewModelFactory.getInstance(
            requireContext(),
            requireContext().datastore
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            viewmodel.listUsers.observe(viewLifecycleOwner) { listUsers ->
                listUsers?.let {
                    adapter = RecyclerViewAdapter(
                        it,
                        FavoriteFragmentDirections::actionFavoriteFragmentToDetailUserFragment
                    )
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getUsers(requireContext())
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            viewmodel.getUsers(requireContext())
        }

        viewmodel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.list.visibility = View.GONE
                binding.layoutShimmer.apply {
                    visibility = View.VISIBLE
                    startShimmer()
                }
            } else {
                binding.list.visibility = View.VISIBLE
                binding.swipeRefresh.isRefreshing = false
                binding.layoutShimmer.apply {
                    visibility = View.GONE
                    stopShimmer()
                }
            }
        }

        viewmodel.error.observe(viewLifecycleOwner) { error ->
            binding.layoutShimmer.apply {
                visibility = View.GONE
                stopShimmer()
            }
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
        }
    }
}