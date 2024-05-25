package com.example.githubapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.githubapp.R
import com.example.githubapp.databinding.FragmentLoginBinding
import com.example.githubapp.ui.utill.generateToken
import com.example.githubapp.ui.viewmodel.LoginViewModel
import com.example.githubapp.ui.viewmodel.LoginViewModelFactory
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewmodel: LoginViewModel by viewModels {
        LoginViewModelFactory.getInstance(
            requireContext(),
            requireContext().datastore
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentLoginBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnTextBuatAkun.setOnClickListener {
            binding.root.findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
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
                    binding.root.findNavController()
                        .navigate(
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
}