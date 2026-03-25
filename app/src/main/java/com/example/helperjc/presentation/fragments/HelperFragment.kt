package com.example.helperapp.presentation.fragments

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
import com.example.helperapp.R
import com.example.helperapp.databinding.FragmentHelperBinding
import com.example.helperjc.domain.plans.Plan

import com.example.helperjc.presentation.factories.ViewModelFactory
import com.example.helperapp.presentation.recyclerviewutils.adapters.PlanDetailsListAdapter
import com.example.helperjc.presentation.states.HelperState
import com.example.helperapp.presentation.viewmodels.HelperViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


class HelperFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewmodel by lazy {
        ViewModelProvider.create(this, viewModelFactory)[HelperViewModel::class]
    }

    private var _binding: FragmentHelperBinding? = null
    private val binding: FragmentHelperBinding
        get() = _binding ?: throw RuntimeException("FragmentHelperBinding is null")
    private val planAdapter by lazy {
        PlanDetailsListAdapter()
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
        _binding = FragmentHelperBinding.inflate(
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
        observeViewModel()

        binding.imageTasks.setOnClickListener {
            launchTodoListFragment()
        }
        binding.floatingActionButton.setOnClickListener {
            launchAddPlanBottomFragment()
        }
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
                        is HelperState.DataLoaded -> {
                            binding.progressBar.isVisible = false
                            binding.floatingActionButton.isEnabled = true
                            planAdapter.submitList(it.currentList)
                        }

                        HelperState.Initial -> {
                            binding.progressBar.isVisible = true
                            binding.floatingActionButton.isEnabled = false
                        }

                    }
                }
            }
        }
    }

    private fun setupSwipe(rvPlans: RecyclerView?) {
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
                val element = planAdapter.currentList[viewHolder.bindingAdapterPosition]
                viewmodel.deletePlan(element)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvPlans)
    }


    private fun setupRecyclerView() {
        binding.rvPlans.adapter = planAdapter
        setupSwipe(binding.rvPlans)
    }

    private fun setupRecyclerViewClickListeners() {
        planAdapter.onPlanClick = {
            launchPlanDetailsFragment(it.plan.id)
        }
        planAdapter.onPlanLongClick = {
            launchEditPlanBottomFragment(it.plan.id)
        }
        planAdapter.onAddTask = {

        }
    }


    private fun injectDependency() {
        (requireActivity().application as AppApplication).component
            .inject(this)
    }

    private fun launchPlanDetailsFragment(planId: Int = Plan.UNDEFINED_ID) {
        findNavController().navigate(
            HelperFragmentDirections.actionHelperFragmentToPlanDetailsFragment(
                planId
            )
        )
    }

    private fun launchTodoListFragment() {
        findNavController().navigate(R.id.action_helperFragment_to_toDoListFragment)
    }

    private fun launchAddPlanBottomFragment() {
        findNavController().navigate(R.id.action_helperFragment_to_addPlanBottomFragment)
    }

    private fun launchEditPlanBottomFragment(planId: Int = Plan.UNDEFINED_ID) {
        findNavController().navigate(
            HelperFragmentDirections.actionHelperFragmentToEditPlanDetailsDialog(
                planId
            )
        )
    }
}