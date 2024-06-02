package com.example.githubapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.githubapp.R
import com.example.githubapp.databinding.FragmentSplashBinding
import com.example.githubapp.ui.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : Fragment() {
    private val binding by lazy { FragmentSplashBinding.inflate(layoutInflater) }
    private val viewmodel: SplashViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getMode()
        viewmodel.checkLogIn()

        viewmodel.isLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            val options = NavOptions.Builder()
                .setPopUpTo(R.id.splashFragment, true)
                .build()
            binding.splashArt.pauseAnimation()
            if (isLoggedIn) {
                binding.root.findNavController().navigate(
                    SplashFragmentDirections.actionSplashFragmentToHomeFragment(),
                    options
                )
            } else {
                binding.root.findNavController().navigate(
                    SplashFragmentDirections.actionSplashFragmentToLoginFragment(),
                    options
                )
            }
        }

        viewmodel.getMode.observe(viewLifecycleOwner) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        viewmodel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.splashArt.playAnimation()
            }
        }

        viewmodel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
            binding.splashArt.pauseAnimation()
        }
    }
}