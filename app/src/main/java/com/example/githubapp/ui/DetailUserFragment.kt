package com.example.githubapp.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.githubapp.R
import com.example.githubapp.databinding.FragmentDetailUserBinding
import com.example.githubapp.ui.adapter.ViewPagerAdapter
import com.example.githubapp.loadImageUrl
import com.example.githubapp.toDate
import com.example.githubapp.ui.viewmodel.DetailUserViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailUserFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentDetailUserBinding
    private val args: DetailUserFragmentArgs by navArgs()
    private val viewmodel: DetailUserViewModel by viewModel()

    @StringRes
    private val tabTitles = intArrayOf(
        R.string.followers_count,
        R.string.following_count
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.addMenuProvider(this, viewLifecycleOwner)
        return FragmentDetailUserBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getArgs(args)
        viewmodel.getFavoriteList()
        viewmodel.getDetailUser()

        viewmodel.detailUser.observe(viewLifecycleOwner) {
            binding.tvType.text = it?.type ?: "-"
            binding.tvName.text = it?.name ?: "-"
            binding.tvUsername.text = it?.login ?: "-"
            binding.tvCompany.text = it?.company ?: "-"
            binding.tvCreated.text = (it?.createdAt ?: "-").toDate()
            binding.tvLocation.text = it?.location ?: "-"
            binding.tvRepository.text = (it?.publicRepos ?: "-").toString()
            binding.ivAvatar.loadImageUrl(requireContext(), Uri.parse(it?.avatarUrl))

            binding.viewPager.adapter =
                ViewPagerAdapter(
                    this, listOf(
                        FollowerFragment(),
                        FollowingFragment()
                    ),
                    it?.login
                )
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> resources.getString(tabTitles[0], it?.followers)
                    else -> resources.getString(tabTitles[1], it?.following)
                }
            }.attach()
        }

        viewmodel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.layoutContent.visibility = View.GONE
                binding.layoutShimmer.apply {
                    visibility = View.VISIBLE
                    startShimmer()
                }
            } else {
                binding.layoutContent.visibility = View.VISIBLE
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
        menuInflater.inflate(R.menu.menu_fragment_detail, menu)
        val favoriteMenu = menu.findItem(R.id.menu_favorite)

        viewmodel.isFavorited.observe(viewLifecycleOwner) { isFavorited ->
            if (isFavorited) {
                favoriteMenu.setIcon(R.drawable.ic_favorited)
            } else {
                favoriteMenu.setIcon(R.drawable.ic_favorite)
            }
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_favorite -> {
                viewmodel.updateFavorite()
                true
            }

            else -> false
        }
    }
}