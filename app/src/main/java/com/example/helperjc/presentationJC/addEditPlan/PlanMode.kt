package com.example.helperjc.presentationJC.addEditPlan

sealed class PlanMode {
    object Create : PlanMode()
    data class Edit(val id: Int) : PlanMode()
}