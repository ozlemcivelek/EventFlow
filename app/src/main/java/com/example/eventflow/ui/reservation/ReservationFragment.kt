package com.example.eventflow.ui.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.eventflow.adapter.ReservationAdapter
import com.example.eventflow.common.BaseFragment
import com.example.eventflow.databinding.FragmentReservationBinding
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
        _binding = FragmentReservationBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
       }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.reservationListRecyclerView.adapter = reservationAdapter

        viewModel.getReservations()
        viewModel.reservationModel.observe(viewLifecycleOwner) {
            reservationAdapter.submitList(it)
        }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                reservationAdapter.filterList(newText ?: "")
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}