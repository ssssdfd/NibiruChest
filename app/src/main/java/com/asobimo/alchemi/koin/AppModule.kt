package com.asobimo.alchemi.koin

import com.asobimo.alchemi.sourcedata.FileUtils
import com.asobimo.alchemi.sourcedata.FileUtilsImpl
import com.asobimo.alchemi.sourcedata.MyViewModel
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