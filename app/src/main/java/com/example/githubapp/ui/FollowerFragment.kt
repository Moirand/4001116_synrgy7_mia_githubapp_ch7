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

class FollowerFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private val viewmodel: FollowViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentFollowBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ViewPagerAdapter.USERNAME)
        viewmodel.getUsername(username)
        viewmodel.getFollowers(binding.root.context)

        with(binding.root) {
            layoutManager = LinearLayoutManager(context)
            viewmodel.listFollowers.observe(viewLifecycleOwner) { listUsers ->
                adapter = listUsers?.let {
                    RecyclerViewAdapter(
                        it,
                        DetailUserFragmentDirections::actionDetailUserFragmentSelf
                    )
                }
            }
        }

        viewmodel.error.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }
}