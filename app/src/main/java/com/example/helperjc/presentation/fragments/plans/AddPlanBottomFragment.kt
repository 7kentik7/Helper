package com.example.helperjc.presentation.fragments.plans

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.helperapp.AppApplication
import com.example.helperapp.databinding.AddPlanBottomDialogBinding
import com.example.helperjc.enums.PlanColor
import com.example.helperapp.parseToString
import com.example.helperjc.presentation.factories.ViewModelFactory
import com.example.helperapp.presentation.fragments.DatePickerFragment
import com.example.helperjc.presentation.states.plans.AddPlanState
import com.example.helperjc.presentation.viewmodels.plans.AddPlanViewModel
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class AddPlanBottomFragment : DialogFragment(),
    DatePickerFragment.OnDateSelectedListener {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewmodel by lazy {
        ViewModelProvider.create(this, viewModelFactory)[AddPlanViewModel::class]
    }

    private var _binding: AddPlanBottomDialogBinding? = null
    private val binding: AddPlanBottomDialogBinding
        get() = _binding ?: throw RuntimeException("AddPlanBottomDialogBinding is null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddPlanBottomDialogBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setUserInputListeners()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDataSelected(year: Int, month: Int, day: Int) {
        binding.dateChip.visibility = View.VISIBLE
        val date = LocalDate.of(year, month, day)
        val time = LocalTime.now()
        val formattedDate = LocalDateTime.of(date, time)
        binding.dateChip.text = formattedDate.parseToString()
        viewmodel.onEndTimeChange(formattedDate)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.state.collect {
                    when (it) {
                        is AddPlanState.Data -> {
                            if (binding.etTitle.text.toString() != it.title) {
                                binding.etTitle.setText(it.title)
                            }
                            if (binding.dateChip.text.toString() != it.endTime?.parseToString()) {
                                binding.dateChip.text = it.endTime?.parseToString()
                            }
                            if (it.endTime == null) {
                                binding.dateChip.visibility = Chip.GONE
                            } else {
                                binding.dateChip.visibility = Chip.VISIBLE
                            }
                            binding.radioBottonDefaultColor.isChecked =
                                it.color == PlanColor.Default

                        }

                        is AddPlanState.Error -> {
                            makeToast(it.messageResId)
                        }

                        AddPlanState.Success -> {
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.dateChip.setOnClickListener {
            viewmodel.onEndTimeDelete()
        }
        binding.tvSave.setOnClickListener {
            viewmodel.onSaveButtonClick()
        }
        binding.imageSetDate.setOnClickListener {
            val datePicker = DatePickerFragment()
            datePicker.setListener(this)
            datePicker.show(parentFragmentManager, "datePicker")
        }
        binding.radioBottonDefaultColor.setOnClickListener {
            viewmodel.onColorChanged(PlanColor.Default)
        }
        binding.radioBottonBlueColor.setOnClickListener {
            viewmodel.onColorChanged(PlanColor.BLUE)
        }
        binding.radioBottonTealColor.setOnClickListener {
            viewmodel.onColorChanged(PlanColor.TEAL)
        }
        binding.radioBottonPurpleColor.setOnClickListener {
            viewmodel.onColorChanged(PlanColor.PURPLE)
        }
        binding.radioBottonPinkColor.setOnClickListener {
            viewmodel.onColorChanged(PlanColor.PINK)
        }
        binding.radioBottonOrangeColor.setOnClickListener {
            viewmodel.onColorChanged(PlanColor.ORANGE)
        }
        binding.radioBottonImeColor.setOnClickListener {
            viewmodel.onColorChanged(PlanColor.IME)
        }
    }

    private fun setUserInputListeners() {
        binding.etTitle.doAfterTextChanged { text ->
            viewmodel.onTitleChange(text.toString())
        }
    }

    private fun makeToast(resId: Int) {
        Toast.makeText(
            requireContext(),
            resId,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun injectDependency() {
        (requireActivity().application as AppApplication).component
            .inject(this)
    }
}