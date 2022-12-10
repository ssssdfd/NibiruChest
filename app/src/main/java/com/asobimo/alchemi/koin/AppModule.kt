package com.asobimo.alchemi.koin

import com.asobimo.alchemi.internal.FileUtils
import com.asobimo.alchemi.internal.FileUtilsImpl
import com.asobimo.alchemi.MyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<FileUtils> {
        FileUtilsImpl(get())
    }

    viewModel{
        MyViewModel(get())
    }
}