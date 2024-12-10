package com.example.eventflow.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.eventflow.adapter.EventAdapter
import com.example.eventflow.databinding.FragmentHomeBinding
import com.example.eventflow.ui.SharedViewModel
import java.util.Calendar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val eventAdapter = EventAdapter()

    private val viewModel by viewModels<HomeViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = eventAdapter

        viewModel.getEvents {
            setFilteredEventsForDate() // Veriler geldikten sonra filtreleme işlemini başlat
        }

        // Observe filtered Events in viewModel
        viewModel.filteredEvents.observe(viewLifecycleOwner) { filteredEvents ->
            eventAdapter.setItems(filteredEvents)
        }
    }

    private fun setFilteredEventsForDate() {
        val calendarView: CalendarView = binding.calendarView

        // initial Date and Filtered
        viewModel.filterEventsByDate(calendarView.date)
        // CalendarView filter on date change
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val preparedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            sharedViewModel.setCalendarTime(preparedDate.timeInMillis)
            viewModel.filterEventsByDate(preparedDate.timeInMillis)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}