package com.example.helperjc.di

import android.app.Application
import com.example.helperjc.di.modules.DataModule
import com.example.helperjc.di.modules.DomainModule
import com.example.helperjc.di.modules.ViewModelModule
import com.example.helperjc.di.scopes.HelperScope
import com.example.helperjc.presentation.fragments.plans.AddPlanBottomFragment
import com.example.helperapp.presentation.fragments.tasks.AddTaskBottomFragment
import com.example.helperapp.presentation.fragments.tasks.AddTaskForPlanBottomFragment
import com.example.helperapp.presentation.fragments.plans.EditPlanDetailsDialog
import com.example.helperapp.presentation.fragments.HelperFragment
import com.example.helperapp.presentation.fragments.plans.PlanDetailsFragment
import com.example.helperapp.presentation.fragments.tasks.TasksFragment
import dagger.BindsInstance
import dagger.Component

@HelperScope
@Component(modules = [DataModule::class, DomainModule::class, ViewModelModule::class])
interface HelperComponent {
    fun inject(addTaskBottomFragment: AddTaskBottomFragment)
    fun inject(addPlanBottomFragment: AddPlanBottomFragment)
    fun inject(helperFragment: HelperFragment)
    fun inject(tasksFragment: TasksFragment)
    fun inject(planDetailsFragment: PlanDetailsFragment)
    fun inject(addTaskForPlanBottomFragment: AddTaskForPlanBottomFragment)
    fun inject(editPlanDetails: EditPlanDetailsDialog)

    @Component.Factory
    interface HelperComponentFactory {
        fun create(@BindsInstance application: Application): HelperComponent
    }
}