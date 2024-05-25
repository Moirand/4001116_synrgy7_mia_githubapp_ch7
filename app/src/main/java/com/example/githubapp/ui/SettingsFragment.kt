package com.example.githubapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.githubapp.databinding.FragmentSettingsBinding
import com.example.githubapp.ui.viewmodel.SettingsViewModel
import com.example.githubapp.ui.viewmodel.SettingsViewModelFactory

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewmodel: SettingsViewModel by viewModels {
        SettingsViewModelFactory.getInstance(
            requireContext().datastore
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSettingsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.getMode()
        viewmodel.getMode.observe(viewLifecycleOwner) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchMode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchMode.isChecked = false
            }
        }

        binding.switchMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked ->
            viewmodel.setMode(isChecked)
        }

        viewmodel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
    }
}