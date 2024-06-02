package com.example.githubapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.githubapp.R
import com.example.githubapp.databinding.FragmentRegisterBinding
import com.example.githubapp.ui.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {
    private val binding by lazy { FragmentRegisterBinding.inflate(layoutInflater) }
    private val viewmodel: RegisterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.checkEmail(binding.edtEmail.text.toString())

        binding.edtUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(username: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewmodel.checkUsername(username.toString())
                viewmodel.isUsernameExist.observe(viewLifecycleOwner) { isUsernameExist ->
                    binding.ilUsername.error =
                        if (isUsernameExist) "Username sudah terdaftar" else null
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(email: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewmodel.checkEmail(email.toString())
                viewmodel.isEmailExist.observe(viewLifecycleOwner) { isEmailExist ->
                    binding.ilEmail.error = if (isEmailExist) "Email sudah terdaftar" else null
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.btnSignup.setOnClickListener {
            if (viewmodel.isUsernameExist.value == false || viewmodel.isEmailExist.value == false) {
                viewmodel.register(
                    username = binding.edtUsername.text.toString(),
                    email = binding.edtEmail.text.toString(),
                    password = binding.edtPassword.text.toString()
                )
            } else {
                Toast.makeText(requireContext(), "Username atau Email sudah terdaftar", Toast.LENGTH_LONG).show()
            }
        }

        viewmodel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                clearStackAndMoveToSuccessFragment()
            }
        }

        viewmodel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.flipperBtnRegister.displayedChild = 1
            } else {
                binding.flipperBtnRegister.displayedChild = 0
            }
        }

        viewmodel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun clearStackAndMoveToSuccessFragment() {
        val options = NavOptions.Builder()
            .setPopUpTo(R.id.registerFragment, true)
            .build()
        binding.root.findNavController().navigate(
            RegisterFragmentDirections.actionRegisterFragmentToSuccessRegistrationFragment(),
            options
        )
    }
}