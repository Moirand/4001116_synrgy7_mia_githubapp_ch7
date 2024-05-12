package com.example.githubapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.githubapp.R
import com.example.githubapp.databinding.FragmentDetailUserBinding
import com.example.githubapp.ui.adapter.ViewPagerAdapter
import com.example.githubapp.ui.utill.toDate
import com.example.githubapp.ui.utill.loadImageUrl
import com.example.githubapp.ui.viewmodel.DetailUserViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserFragment : Fragment() {
    private lateinit var binding: FragmentDetailUserBinding
    private val args: DetailUserFragmentArgs by navArgs()
    private val viewmodel: DetailUserViewModel by viewModels()

    @StringRes
    private val tabTitles = intArrayOf(
        R.string.followers_count,
        R.string.following_count
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDetailUserBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getArgs(args)
        viewmodel.getDetailUser(binding.root.context)

        viewmodel.detailUser.observe(viewLifecycleOwner) {
            binding.tvType.text = it?.type ?: "-"
            binding.tvName.text = it?.name ?: "-"
            binding.tvUsername.text = it?.login ?: "-"
            binding.tvCompany.text = it?.company ?: "-"
            binding.tvCreated.text = (it?.createdAt ?: "-").toDate()
            binding.tvLocation.text = it?.location ?: "-"
            binding.tvRepository.text = (it?.publicRepos ?: "-").toString()
            binding.ivAvatar.loadImageUrl(requireContext(), it?.avatarUrl)

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

        viewmodel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }
}