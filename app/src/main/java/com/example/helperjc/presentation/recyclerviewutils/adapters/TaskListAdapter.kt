package com.example.helperapp.presentation.recyclerviewutils.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

import androidx.recyclerview.widget.ListAdapter
import com.example.helperapp.R
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.enums.TaskPriority
import com.example.helperapp.presentation.recyclerviewutils.diffcallback.TaskListDiffCallback
import com.example.helperapp.presentation.recyclerviewutils.holders.TaskViewHolder

class TaskListAdapter : ListAdapter<Task, TaskViewHolder>(TaskListDiffCallback()) {
    var onSwitchTaskState: ((Task) -> Unit)? = null
    var onTaskClickListener: ((Task) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_ACTIVE -> {
                R.layout.todo_item_active
            }

            VIEW_TYPE_COMPLETED -> {
                R.layout.todo_item_completed
            }

            else -> {
                throw RuntimeException("Unknown viewType:$viewType")
            }
        }
        val view = LayoutInflater.from(parent.context).inflate(
            layout,
            parent,
            false
        )
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(
        viewHolder: TaskViewHolder,
        position: Int
    ) {
        val task = getItem(position)

        viewHolder.tvTitle.text = task.title
        viewHolder.tvDescription.text = task.description

        viewHolder.radioButton.setOnClickListener {
            onSwitchTaskState?.invoke(task)
        }
        viewHolder.tvTitle.setOnClickListener {
            onTaskClickListener?.invoke(task)
        }
        val drawable = ContextCompat.getDrawable(viewHolder.itemView.context, getPriorityRes(task))
        viewHolder.viewPriority.background = drawable

        if (task.isActive) {
            viewHolder.radioButton.isChecked = false
        } else {
            viewHolder.radioButton.isChecked = true
            viewHolder.tvTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            viewHolder.tvDescription.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
        viewHolder.tvDescription.isVisible = !task.description.isNullOrBlank()
    }

    private fun getPriorityRes(task: Task): Int {
        return when (task.priority) {
            TaskPriority.LOW -> {
                if (task.isActive) {
                    R.drawable.task_low_priority
                } else {
                    R.drawable.task_low_priority_complete
                }
            }

            TaskPriority.MEDIUM -> {
                if (task.isActive) {
                    R.drawable.task_medium_priority
                } else {
                    R.drawable.task_medium_priority_complete
                }
            }

            TaskPriority.HIGH -> {
                if (task.isActive) {
                    R.drawable.task_high_priority
                } else {
                    R.drawable.task_high_priority_complete
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        if (item.isActive) {
            return VIEW_TYPE_ACTIVE
        } else {
            return VIEW_TYPE_COMPLETED
        }
    }

    companion object {
        const val VIEW_TYPE_ACTIVE = 100
        const val VIEW_TYPE_COMPLETED = 101
    }
}