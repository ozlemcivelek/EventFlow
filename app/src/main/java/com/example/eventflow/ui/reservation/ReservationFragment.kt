package com.example.eventflow.ui.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.eventflow.adapter.ReservationAdapter
import com.example.eventflow.common.BaseFragment
import com.example.eventflow.databinding.FragmentReservationBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class ReservationFragment : BaseFragment<ReservationViewModel>() {

    private var _binding: FragmentReservationBinding? = null
    private val binding get() = _binding!!

    private val reservationAdapter = ReservationAdapter()

    override val viewModel: ReservationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReservationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.reservationListRecyclerView.adapter = reservationAdapter

        viewModel.getReservations()

        viewModel.emptyState.observe(viewLifecycleOwner) {
            binding.emptyState.isVisible = it
            binding.reservationListRecyclerView.isVisible = !it
        }


        viewModel.tabPosition.observe(viewLifecycleOwner) {
            if (it == 0) {
                viewModel.emptyState.value = viewModel.upcomingReservations.isEmpty()
                reservationAdapter.submitList(viewModel.upcomingReservations)
            } else {
                viewModel.emptyState.value = viewModel.pastReservations.isEmpty()
                reservationAdapter.submitList(viewModel.pastReservations)
            }
        }
        setupTabLayout()

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                reservationAdapter.filterList(newText ?: "")
                return true
            }
        })
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.tabPosition.value = tab?.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}