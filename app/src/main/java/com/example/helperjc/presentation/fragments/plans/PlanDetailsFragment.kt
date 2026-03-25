package com.example.helperapp.presentation.fragments.plans

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.helperapp.AppApplication
import com.example.helperapp.databinding.PlanDetailsFragmentBinding
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.presentation.factories.ViewModelFactory
import com.example.helperapp.presentation.fragments.PlanDetailsFragmentArgs
import com.example.helperapp.presentation.fragments.PlanDetailsFragmentDirections
import com.example.helperapp.presentation.recyclerviewutils.adapters.TaskListAdapter
import com.example.helperjc.presentation.states.plans.PlanDetailsState
import com.example.helperjc.presentation.viewmodels.plans.PlanDetailsViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlanDetailsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val args by navArgs<PlanDetailsFragmentArgs>()
    private val viewmodel by lazy {
        ViewModelProvider.Companion.create(this, viewModelFactory)[PlanDetailsViewModel::class.java]
    }
    private var _binding: PlanDetailsFragmentBinding? = null
    private val binding: PlanDetailsFragmentBinding
        get() = _binding ?: throw RuntimeException("PlanDetailsFragmentBinding is null")

    private val tasksAdapter by lazy {
        TaskListAdapter()
    }

    override fun onAttach(context: Context) {
        injectDependency()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PlanDetailsFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupRecyclerViewClickListeners()
        setupClickListeners()
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
                        is PlanDetailsState.DataLoaded -> {

                            if (binding.toolbar.title.toString() != it.plan.title) {
                                binding.toolbar.title = it.plan.title
                            }
                            if (it.tasksList.isEmpty()) {
                                binding.tvEmptyList.isVisible = true
                            } else {
                                binding.tvEmptyList.isVisible = false
                            }
                            binding.progressBar.isVisible = false
                            binding.floatingActionButton.isEnabled = true
                            tasksAdapter.submitList(it.tasksList)
                        }

                        PlanDetailsState.Initial -> {
                            binding.progressBar.isVisible = true
                            binding.floatingActionButton.isEnabled = false
                            viewmodel.loadData(args.planId)
                        }

                        PlanDetailsState.Success -> {
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        with(binding) {
            rvTasks.adapter = tasksAdapter
            setupSwipe(rvTasks)
        }
    }

    private fun setupClickListeners() {
        binding.floatingActionButton.setOnClickListener {
            launchAddEditTaskScreen()
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerViewClickListeners() {
        tasksAdapter.onSwitchTaskState = {
            viewmodel.changeEnableState(task = it, planId = args.planId)
        }
        tasksAdapter.onTaskClickListener = {
            launchAddEditTaskScreen(taskId = it.id)
        }
    }

    private fun setupSwipe(rvTasks: RecyclerView?) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                val element = tasksAdapter.currentList[viewHolder.bindingAdapterPosition]
                viewmodel.deleteTask(element)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvTasks)
    }

    private fun injectDependency() {
        (requireActivity().application as AppApplication).component
            .inject(this)
    }

    private fun launchAddEditTaskScreen(
        taskId: Int = Task.UNDEFINED_ID,
        planId: Int = args.planId
    ) {
        findNavController().navigate(
            PlanDetailsFragmentDirections.actionPlanDetailsFragmentToAddTaskForPlanBottomFragment(
                taskId,
                planId
            )
        )
    }
}