package com.example.helperapp.presentation.fragments.tasks

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.helperapp.AppApplication
import com.example.helperapp.databinding.FragmentTasksBinding
import com.example.helperjc.domain.tasks.Task
import com.example.helperjc.presentation.factories.ViewModelFactory
import com.example.helperapp.presentation.fragments.TasksFragmentDirections
import com.example.helperapp.presentation.recyclerviewutils.adapters.TaskListAdapter
import com.example.helperjc.presentation.states.tasks.TaskListState
import com.example.helperjc.presentation.viewmodels.tasks.TodoListViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class TasksFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewmodel by lazy {
        ViewModelProvider.create(this, viewModelFactory)[TodoListViewModel::class]
    }
    private var _binding: FragmentTasksBinding? = null
    private val binding: FragmentTasksBinding
        get() = _binding ?: throw RuntimeException("FragmentTasksBinding is null")
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
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTasksBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        setupClickListeners()
        setupRecyclerViewClickListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.state.collect {
                    when (it) {
                        is TaskListState.DataLoaded -> {
                            binding.progressBar.isVisible = false
                            binding.floatingActionButton.isEnabled = true
                            setupTasks(it.currencyList)
                        }

                        TaskListState.Initial -> {
                            binding.progressBar.isVisible = true
                            binding.floatingActionButton.isEnabled = false
                        }
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.floatingActionButton.setOnClickListener {
            launchAddEditTaskDialog()
        }
        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerViewClickListeners() {
        tasksAdapter.onSwitchTaskState = {
            viewmodel.changeEnableState(it)
        }
        tasksAdapter.onTaskClickListener = {
            launchAddEditTaskDialog(it.id)
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

    private fun setupTasks(tasks: List<Task>) {
        tasksAdapter.submitList(tasks)
    }

    private fun setupRecyclerView() {
        with(binding.rvTasks) {
            adapter = tasksAdapter
        }
        setupSwipe(binding.rvTasks)
        setupRecyclerViewClickListeners()
    }

    private fun injectDependency() {
        (requireActivity().application as AppApplication).component
            .inject(this)
    }

    private fun launchAddEditTaskDialog(
        taskId: Int = Task.UNDEFINED_ID,
    ) {
        findNavController().navigate(
            TasksFragmentDirections.actionToDoListFragmentToAddTaskBottomFragment(
                taskId
            )
        )
    }
}