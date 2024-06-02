package com.example.githubapp.di

import com.example.githubapp.ui.viewmodel.DetailUserViewModel
import com.example.githubapp.ui.viewmodel.FavoriteViewModel
import com.example.githubapp.ui.viewmodel.FollowViewModel
import com.example.githubapp.ui.viewmodel.HomeViewModel
import com.example.githubapp.ui.viewmodel.LoginViewModel
import com.example.githubapp.ui.viewmodel.RegisterViewModel
import com.example.githubapp.ui.viewmodel.SettingsViewModel
import com.example.githubapp.ui.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { FollowViewModel(get()) }
    viewModel { FavoriteViewModel(get(), get(), get()) }
    viewModel { DetailUserViewModel(get(), get(), get(), get()) }
}