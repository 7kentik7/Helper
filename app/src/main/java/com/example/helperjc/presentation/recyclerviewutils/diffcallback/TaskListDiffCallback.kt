package com.example.helperapp.presentation.recyclerviewutils.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.example.helperjc.domain.tasks.Task

class TaskListDiffCallback(
) : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(
        oldItem: Task,
        newItem: Task
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Task,
        newItem: Task
    ): Boolean {
        return oldItem == newItem
    }
}