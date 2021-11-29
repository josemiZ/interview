package com.josemiz.interview.application

import android.app.Application

class InterviewApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initDI()
    }
}