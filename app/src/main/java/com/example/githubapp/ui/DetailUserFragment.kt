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
import com.example.githubapp.ui.viewmodel.DetailUserViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailUserFragment : Fragment(), MenuProvider {
    private val binding by lazy { FragmentDetailUserBinding.inflate(layoutInflater) }
    private val viewmodel: DetailUserViewModel by viewModel()
    private val args: DetailUserFragmentArgs by navArgs()

    @StringRes
    private val tabTitles = intArrayOf(
        R.string.followers_count,
        R.string.following_count
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.addMenuProvider(this, viewLifecycleOwner)
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
                    this,
                    it?.login,
                    listOf(
                        FollowerFragment(),
                        FollowingFragment()
                    )
                )
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> resources.getString(tabTitles[0], it?.followers)
                    else -> resources.getString(tabTitles[1], it?.following)
                }
            }.attach()
        }

        viewmodel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
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