package com.example.githubapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.R
import com.example.githubapp.databinding.FragmentHomeBinding
import com.example.githubapp.ui.adapter.RecyclerViewAdapter
import com.example.githubapp.ui.viewmodel.HomeViewModel
import com.example.githubapp.ui.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

class HomeFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentHomeBinding
    private val viewmodel: HomeViewModel by viewModels {
        HomeViewModelFactory.getInstance(
            requireContext(),
            requireContext().datastore
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.addMenuProvider(this, viewLifecycleOwner)

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

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_fragment_home, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_settings -> {
                binding.root.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
                )
                true
            }

            R.id.menu_search -> {
                (menuItem.actionView as SearchView).apply {
                    isFocusable = true
                    isIconified = false
                    queryHint = "Ketikkan Username"

                    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(username: String): Boolean {
                            viewmodel.getUsers(requireContext(), username)
                            clearFocus()
                            return true
                        }

                        override fun onQueryTextChange(username: String): Boolean {
                            if (username.isBlank()) {
                                viewmodel.getUsers(requireContext())
                            }
                            return true
                        }
                    })
                }
                true
            }

            R.id.menu_delete -> {
                lifecycleScope.launch {
                    viewmodel.deleteAccount()
                    awaitAll(viewmodel.signOut())
                    clearStackAndMoveToLoginFragment()
                }
                true
            }

            R.id.menu_signout -> {
                lifecycleScope.launch {
                    awaitAll(viewmodel.signOut())
                    clearStackAndMoveToLoginFragment()
                }
                true
            }

            else -> false
        }
    }

    private fun clearStackAndMoveToLoginFragment() {
        val options = NavOptions.Builder()
            .setPopUpTo(R.id.homeFragment, true)
            .build()
        binding.root.findNavController()
            .navigate(
                HomeFragmentDirections.actionHomeFragmentToLoginFragment(),
                options
            )
    }
}