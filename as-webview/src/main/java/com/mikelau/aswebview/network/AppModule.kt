package com.mikelau.aswebview.network

import com.mikelau.aswebview.viewmodels.InAppWebViewViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val asWebViewAppModule = module {

    viewModel {
        InAppWebViewViewModel(get())
    }

}