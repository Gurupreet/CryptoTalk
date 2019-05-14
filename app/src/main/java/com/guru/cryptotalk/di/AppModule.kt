package com.guru.cryptotalk.di

import com.guru.cryptotalk.ui.login.LoginViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val AppModule = module {
    viewModel { LoginViewModel() }
}