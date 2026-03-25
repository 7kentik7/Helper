package com.example.helperapp.presentation.recyclerviewutils.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.example.helperjc.domain.plandetails.PlanDetails

class PlanListDiffCallback() : DiffUtil.ItemCallback<PlanDetails>() {
    override fun areItemsTheSame(
        oldItem: PlanDetails,
        newItem: PlanDetails
    ): Boolean {
        return oldItem.plan.id == newItem.plan.id
    }

    override fun areContentsTheSame(
        oldItem: PlanDetails,
        newItem: PlanDetails
    ): Boolean {
        return newItem == oldItem
    }
}