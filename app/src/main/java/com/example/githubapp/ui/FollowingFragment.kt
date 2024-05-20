package com.example.githubapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.databinding.FragmentFollowBinding
import com.example.githubapp.ui.adapter.RecyclerViewAdapter
import com.example.githubapp.ui.adapter.ViewPagerAdapter
import com.example.githubapp.ui.viewmodel.FollowViewModel

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private val viewmodel: FollowViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            viewmodel.listFollowing.observe(viewLifecycleOwner) { listUsers ->
                adapter = listUsers?.let {
                    RecyclerViewAdapter(
                        it,
                        DetailUserFragmentDirections::actionDetailUserFragmentSelf
                    )
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ViewPagerAdapter.USERNAME)
        viewmodel.getUsername(username)
        viewmodel.getFollowing(binding.root.context)

        viewmodel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.list.removeAllViewsInLayout()
                binding.layoutShimmer.apply {
                    visibility = View.VISIBLE
                    startShimmer()
                }
            } else {
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
            Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
        }
    }
}