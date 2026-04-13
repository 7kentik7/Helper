package com.example.helperjc

import com.example.helperjc.domain.plans.Plan
import com.example.helperjc.presentationJC.addEditPlan.AddPlanState

fun AddPlanState.toDomain(): Plan =
    Plan(title = this.title.trim(), color = this.color, endTime = this.endTime?.parseToLocalDateTime())