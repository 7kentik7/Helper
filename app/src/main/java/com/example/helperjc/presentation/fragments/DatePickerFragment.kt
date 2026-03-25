package com.example.helperapp.presentation.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker

import androidx.fragment.app.DialogFragment
import com.example.helperapp.R
import java.util.Calendar

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var listener: OnDateSelectedListener? = null

    interface OnDateSelectedListener {
        fun onDataSelected(year: Int, month: Int, day: Int)
    }

    fun setListener(listener: OnDateSelectedListener) {
        this.listener = listener
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerDialog,
            this,
            year,
            month,
            day,
        )
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        listener?.onDataSelected(year, month, day)
    }
}