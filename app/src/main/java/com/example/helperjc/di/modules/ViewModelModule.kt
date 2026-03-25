package com.example.helperjc.di.modules

import androidx.lifecycle.ViewModel
import com.example.helperjc.di.keys.ViewModelKey
import com.example.helperjc.presentation.viewmodels.plans.AddPlanViewModel
import com.example.helperjc.presentation.viewmodels.tasks.AddTaskForPlanViewModel
import com.example.helperjc.presentation.viewmodels.tasks.AddTaskViewModel
import com.example.helperjc.presentation.viewmodels.plans.EditPlanDetailsViewModel
import com.example.helperapp.presentation.viewmodels.HelperViewModel
import com.example.helperjc.presentation.viewmodels.plans.PlanDetailsViewModel
import com.example.helperjc.presentation.viewmodels.tasks.TodoListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AddTaskViewModel::class)
    fun bindAddTaskViewModel(addTaskViewModel: AddTaskViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TodoListViewModel::class)
    fun bindTodoListViewModel(todoListViewModel: TodoListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HelperViewModel::class)
    fun bindHelperViewModel(helperViewModel: HelperViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddPlanViewModel::class)
    fun bindAddPlanViewModel(addPlanViewModel: AddPlanViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlanDetailsViewModel::class)
    fun bindPlanDetailsViewModel(planDetailsViewModel: PlanDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddTaskForPlanViewModel::class)
    fun bindAddTaskForPlanViewModel(addTaskForPlanViewModel: AddTaskForPlanViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditPlanDetailsViewModel::class)
    fun bindEditPlanDetailsViewModel(editPlanDetailsViewModel: EditPlanDetailsViewModel): ViewModel
}