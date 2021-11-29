package com.josemiz.interview.application

import android.app.Application
import com.josemiz.interview.views.main.MainActivity
import com.josemiz.interview.views.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun Application.initDI() {
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        modules(listOf(scopedModule))
    }
}

private val scopedModule = module {
    scope(named<MainActivity>()) {
        viewModel { MainViewModel() }
    }
}