package com.example.helperapp.presentation.recyclerviewutils.adapters


import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import com.example.helperapp.R
import com.example.helperapp.databinding.PlannerItemBinding
import com.example.helperjc.domain.plandetails.PlanDetails
import com.example.helperjc.enums.PlanColor
import com.example.helperapp.parseToString
import com.example.helperapp.presentation.recyclerviewutils.diffcallback.PlanListDiffCallback
import com.example.helperapp.presentation.recyclerviewutils.holders.PlanViewHolder

class PlanDetailsListAdapter : ListAdapter<PlanDetails, PlanViewHolder>(PlanListDiffCallback()) {
    var onPlanClick: ((PlanDetails) -> Unit)? = null
    var onPlanLongClick: ((PlanDetails) -> Unit)? = null
    var onAddTask: ((PlanDetails) -> Unit)? = null

    var count = 0
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlanViewHolder {
        val binding = PlannerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlanViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlanViewHolder,
        position: Int
    ) {
        val planDetails = getItem(position)
        with(holder.binding) {
            tvTitle.text = planDetails.plan.title
            tvDate.text =
                planDetails.plan.endTime?.parseToString()

            progressBarPlan.progress = planDetails.progress
            val colorProgress = ContextCompat.getColor(
                holder.itemView.context,
                getProgressColorResId(planDetails.progress)
            )
            progressBarPlan.progressTintList = ColorStateList.valueOf(colorProgress)

            val colorPlan = ContextCompat.getColor(
                holder.itemView.context,
                getPlanColorResId(planDetails.plan.color)
            )
            cardPlan.strokeColor = colorPlan
            if (tvDate.text.isEmpty()) {
                tvDate.isVisible = false
            }

            if (planDetails.countOfTasks == 0) {
                tvCountOfTasks.isVisible = false
                tvCountOfCompletedTasks.isVisible = false
                tvEmptyCountOfTasks.isVisible = true
            } else {
                tvCountOfTasks.isVisible = true
                tvCountOfCompletedTasks.isVisible = true
                tvEmptyCountOfTasks.isVisible = false
            }

            tvCountOfTasks.text = holder.itemView.context.getString(
                R.string.tasks_count,
                planDetails.countOfTasks,
            )
            tvCountOfCompletedTasks.text = holder.itemView.context.getString(
                R.string.completed_tasks_count,
                planDetails.countOfCompletedTasks,
            )

            clPlan.setOnClickListener {
                onPlanClick?.invoke(planDetails)
            }
            clPlan.setOnLongClickListener {
                onPlanLongClick?.invoke(planDetails)
                true
            }
            imageViewAddTask.setOnClickListener {
                onAddTask?.invoke(planDetails)
            }
            Log.d("Adapter", "${count++}")
        }
    }

    private fun getPlanColorResId(planColor: PlanColor): Int {
        return when (planColor) {
            PlanColor.Default -> R.color.plan_color_default
            PlanColor.BLUE -> R.color.plan_color_blue
            PlanColor.TEAL -> R.color.plan_color_teal
            PlanColor.PURPLE -> R.color.plan_color_purple
            PlanColor.PINK -> R.color.plan_color_pink
            PlanColor.ORANGE -> R.color.plan_color_orange
            PlanColor.IME -> R.color.plan_color_ime
        }
    }

    private fun getProgressColorResId(progress: Int): Int {
        return when (progress) {
            in 0 until 20 -> {
                R.color.progress_first_step
            }

            in 20 until 40 -> {
                R.color.progress_second_step
            }

            in 40 until 60 -> {
                R.color.progress_third_step
            }

            in 60 until 80 -> {
                R.color.progress_fourth_step
            }

            in 80..100 -> {
                R.color.progress_fifth_step
            }

            else -> {
                R.color.progress_first_step
            }
        }
    }
}