package com.example.eventflow.ui.reservation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.eventflow.adapter.ReservationAdapter
import com.example.eventflow.databinding.FragmentReservationBinding

class ReservationFragment : Fragment() {

    private var _binding: FragmentReservationBinding? = null
    private val binding get() = _binding!!

    private val reservationAdapter = ReservationAdapter()

    private val viewModel by viewModels<ReservationViewModel>()

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

        viewModel.getReservations(
            onSuccess = { reservations ->
                reservationAdapter.submitList(reservations)
            },
            onFailure = { exception ->

            })

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