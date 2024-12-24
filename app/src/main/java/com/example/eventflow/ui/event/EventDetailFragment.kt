package com.example.eventflow.ui.event

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.eventflow.common.BaseFragment
import com.example.eventflow.databinding.FragmentEventDetailBinding
import com.example.eventflow.ui.SharedViewModel
import com.example.eventflow.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class EventDetailFragment : BaseFragment<EventDetailViewModel>() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<EventDetailFragmentArgs>()

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override val viewModel: EventDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getEvent(args.eventId)

        viewModel.eventDetail.observe(viewLifecycleOwner) {
            it ?: return@observe
            binding.eventTitle.text = it.title
            binding.eventDescription.text = it.description
            binding.date.text = it.date
            binding.startHour.text = it.startTime
            binding.endHour.text = it.endTime
            binding.location.text = it.location
            //binding.category.text = event.category
            binding.service.text = ""
            it.serviceList?.forEach {
                binding.service.append(it + "\n")
            }
            binding.customerName.text = it.customerName
            binding.customerPhone.text = it.customerPhone
            binding.customerEmail.text = it.customerEmail

        }

        binding.backButton.setOnClickListener{
            findNavController().popBackStack()
        }
        binding.deleteButton.setOnClickListener{
            //viewModel.deleteEvent(args.eventId)
            //findNavController().popBackStack()
        }
        binding.editButton.setOnClickListener{
            val event = viewModel.eventDetail.value!!
            sharedViewModel.selectedItem(event)
            //event update
            val action = EventDetailFragmentDirections.actionEventDetailFragmentToAddEventFragment()
            findNavController().navigate(action)
        }

    }
}