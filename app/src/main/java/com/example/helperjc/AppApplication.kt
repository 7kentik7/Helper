package com.example.helperjc

import android.app.Application
import com.example.helperjc.di.DaggerHelperComponent


class AppApplication : Application() {
    val component by lazy {
        DaggerHelperComponent.factory().create(this)
    }
}