package com.example.githubapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.githubapp.R
import com.example.githubapp.databinding.FragmentSuccessRegistrationBinding

class SuccessRegistrationFragment : Fragment() {
    private lateinit var binding: FragmentSuccessRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSuccessRegistrationBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnToSignin.setOnClickListener {
            val options = NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build()
            binding.root.findNavController()
                .navigate(
                    SuccessRegistrationFragmentDirections.actionSuccessRegistrationFragmentToLoginFragment(),
                    options
                )
        }
    }
}