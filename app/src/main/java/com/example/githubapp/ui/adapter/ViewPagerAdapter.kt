package com.example.githubapp.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    fragment: Fragment,
    private val fragments: List<Fragment>,
    private val username: String?

) : FragmentStateAdapter(fragment) {

    companion object {
        const val USERNAME = "username"
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position].also {
            it.arguments = Bundle().apply {
                putString(USERNAME, username)
            }
        }
    }
}