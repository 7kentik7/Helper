package com.example.helperapp.presentation.recyclerviewutils.holders

import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.helperapp.R

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val viewPriority = view.findViewById<View>(R.id.viewPriority)
    val tvTitle = view.findViewById<TextView>(R.id.titleTextView)
    val tvDescription = view.findViewById<TextView>(R.id.descriptionTextView)
    val radioButton = view.findViewById<RadioButton>(R.id.todoRadioButton)
}