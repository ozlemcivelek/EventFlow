package com.example.eventflow.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.viewModels
import com.example.eventflow.adapter.EventAdapter
import com.example.eventflow.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val eventAdapter = EventAdapter()

    private val viewModel by viewModels<HomeViewModel>()

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

        setFilteredEventsForDate()
    }

    private fun setFilteredEventsForDate() {
        val calendarView: CalendarView = binding.calendarView
        // initial Date and Filtered
        val initialCalendar = Calendar.getInstance()
        initialCalendar.timeInMillis = calendarView.date
        val initialMonth = SimpleDateFormat("MMM", Locale.ENGLISH).format(initialCalendar.time).uppercase()
        val initialDay = String.format("%02d", initialCalendar.get(Calendar.DAY_OF_MONTH))
        val initialDate = "$initialMonth\n$initialDay"

        viewModel.filterEventsByDate(initialDate)

        // CalendarView filter on date change
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

            val selectedMonth = SimpleDateFormat("MMM", Locale.ENGLISH).format(Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time).uppercase()

            val selectedDate = "$selectedMonth\n${String.format("%02d", dayOfMonth)}"
            viewModel.filterEventsByDate(selectedDate)
        }

        // Observe filtered Events in viewModel
        viewModel.filteredEvents.observe(viewLifecycleOwner) { filteredEvents ->
            eventAdapter.setItems(filteredEvents)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}