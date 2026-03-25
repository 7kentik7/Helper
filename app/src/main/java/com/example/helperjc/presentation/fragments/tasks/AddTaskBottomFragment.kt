package com.example.helperapp.presentation.fragments.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.helperapp.AppApplication
import com.example.helperapp.databinding.AddTaskBottomDialogBinding
import com.example.helperjc.enums.TaskPriority
import com.example.helperjc.presentation.factories.ViewModelFactory
import com.example.helperapp.presentation.fragments.AddTaskBottomFragmentArgs
import com.example.helperjc.presentation.states.tasks.AddTaskState
import com.example.helperjc.presentation.viewmodels.tasks.AddTaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddTaskBottomFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val args by navArgs<AddTaskBottomFragmentArgs>()
    private val viewmodel by lazy {
        ViewModelProvider.create(this, viewModelFactory)[AddTaskViewModel::class.java]
    }
    private var _binding: AddTaskBottomDialogBinding? = null
    private val binding: AddTaskBottomDialogBinding
        get() = _binding ?: throw RuntimeException("AddTaskBottomDialogBinding is null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependency()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddTaskBottomDialogBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        setUserInputListeners()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.state.collect {
                    when (it) {
                        is AddTaskState.DataLoaded -> {
                            if (binding.etTitle.text.toString() != it.title) {
                                binding.etTitle.setText(it.title)
                            }
                            if (binding.etDescription.text.toString() != it.description) {
                                binding.etDescription.setText(it.description)
                            }
                            binding.radioButtonLow.isChecked = it.priority == TaskPriority.LOW
                            binding.radioButtonMedium.isChecked = it.priority == TaskPriority.MEDIUM
                            binding.radioButtonHigh.isChecked = it.priority == TaskPriority.HIGH

                        }

                        AddTaskState.Initial -> {
                            viewmodel.loadData(args.taskId)
                        }

                        AddTaskState.Success -> {
                            findNavController().popBackStack()
                        }

                        is AddTaskState.Error -> {
                            makeToast(it.messageResId)
                        }

                    }
                }
            }
        }
    }

    private fun setUpClickListeners() {
        binding.tvSaveTask.setOnClickListener {
            viewmodel.onSaveButtonClick()
        }
        binding.radioButtonLow.setOnClickListener {
            viewmodel.onPriorityChange(TaskPriority.LOW)
        }
        binding.radioButtonMedium.setOnClickListener {
            viewmodel.onPriorityChange(TaskPriority.MEDIUM)
        }
        binding.radioButtonHigh.setOnClickListener {
            viewmodel.onPriorityChange(TaskPriority.HIGH)
        }
    }

    private fun setUserInputListeners() {
        binding.etTitle.doAfterTextChanged { text ->
            viewmodel.onTitleChange(text.toString())
        }
        binding.etDescription.doAfterTextChanged { text ->
            viewmodel.onDescriptionChange(text.toString())
        }
    }

    private fun injectDependency() {
        (requireActivity().application as AppApplication).component
            .inject(this)
    }

    private fun makeToast(resId: Int) {
        Toast.makeText(
            requireContext(),
            resId,
            Toast.LENGTH_LONG
        ).show()
    }
}