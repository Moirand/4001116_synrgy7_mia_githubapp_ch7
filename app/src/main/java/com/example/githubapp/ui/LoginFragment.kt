package com.example.githubapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.githubapp.R
import com.example.githubapp.databinding.FragmentLoginBinding
import com.example.githubapp.ui.viewmodel.LoginViewModel
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private val viewmodel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCrash.setOnClickListener {
            throw RuntimeException("Test Crash")
        }

        binding.btnTextBuatAkun.setOnClickListener {
            binding.root.findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }

        binding.btnSignin.setOnClickListener {
            viewmodel.login(
                binding.edtEmail.text.toString(),
                binding.edtPassword.text.toString()
            )
        }

        viewmodel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                lifecycleScope.launch {
                    awaitAll(
                        viewmodel.saveToken(generateToken()),
                        viewmodel.saveUserId(binding.edtEmail.text.toString())
                    )
                    val options = NavOptions.Builder()
                        .setPopUpTo(R.id.loginFragment, true)
                        .build()
                    binding.root.findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToHomeFragment(),
                        options
                    )
                }
            } else {
                Toast.makeText(requireContext(), "Akun Belum Terdaftar", Toast.LENGTH_LONG).show()
            }
        }

        viewmodel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.flipperBtnLogin.displayedChild = 1
            } else {
                binding.flipperBtnLogin.displayedChild = 0
            }
        }

        viewmodel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun generateToken(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..20).map { allowedChars.random() }.joinToString("")
    }
}