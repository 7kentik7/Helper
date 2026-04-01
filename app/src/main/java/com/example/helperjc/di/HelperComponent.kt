package com.example.helperjc.di

import android.app.Application
import com.example.helperjc.di.modules.DataModule
import com.example.helperjc.di.modules.DomainModule
import com.example.helperjc.di.modules.ViewModelModule
import com.example.helperjc.di.scopes.HelperScope
import dagger.BindsInstance
import dagger.Component

@HelperScope
@Component(modules = [DataModule::class, DomainModule::class, ViewModelModule::class])
interface HelperComponent {
    @Component.Factory
    interface HelperComponentFactory {
        fun create(@BindsInstance application: Application): HelperComponent
    }
}