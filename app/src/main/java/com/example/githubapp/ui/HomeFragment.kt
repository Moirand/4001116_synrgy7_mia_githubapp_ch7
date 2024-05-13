package com.example.githubapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.databinding.FragmentHomeBinding
import com.example.githubapp.ui.adapter.RecyclerViewAdapter
import com.example.githubapp.ui.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewmodel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            viewmodel.listUsers.observe(viewLifecycleOwner) { listUsers ->
                listUsers?.let {
                    adapter = RecyclerViewAdapter(
                        it,
                        HomeFragmentDirections::actionHomeFragmentToDetailUserFragment
                    )
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getAllUsers(requireContext())

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            viewmodel.getAllUsers(requireContext())
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

        viewmodel.error.observe(viewLifecycleOwner) { message ->
            binding.layoutShimmer.apply {
                visibility = View.GONE
                stopShimmer()
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }
}