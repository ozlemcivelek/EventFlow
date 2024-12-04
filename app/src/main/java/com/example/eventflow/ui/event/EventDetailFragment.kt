package com.example.eventflow.ui.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.eventflow.databinding.FragmentEventDetailBinding
import com.example.eventflow.ui.customer.CustomerEditBottomSheet
import kotlin.getValue

class EventDetailFragment : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<EventDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.eventTitleEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setTitle(text.toString())
        }

        binding.eventDateEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setDate(text.toString())
        }

        binding.eventStartTimeEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setStartTime(text.toString())
        }

        binding.eventEndTimeEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setEndTime(text.toString())
        }

        binding.eventLocationEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setLocation(text.toString())
        }

        binding.customerEditButton.setEndIconOnClickListener {

            val bottomSheet = CustomerEditBottomSheet.Companion.newInstance()
            bottomSheet.onSaveClicked = { customer ->
                viewModel.saveCustomers(customer, onSuccess = {
                    Toast.makeText(requireContext(), "Etkinlik kaydedildi", Toast.LENGTH_SHORT)
                        .show()
                }, onFailure = { exception ->
                    Toast.makeText(
                        requireContext(),
                        "Hata: ${exception.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                })

            }

            bottomSheet.show(parentFragmentManager, "CustomerEditBottomSheet")
        }

        binding.eventSaveButton.setOnClickListener {
            // TODO: Null ve empty kontrollerini yap
            if (!viewModel.isDataValid()) {
                Toast.makeText(
                    requireContext(),
                    "Eksik alanlar var, kontrol et",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            viewModel.saveEvent(onSuccess = {
                Toast.makeText(requireContext(), "Etkinlik kaydedildi", Toast.LENGTH_SHORT).show()
            }, onFailure = { exception ->
                Toast.makeText(requireContext(), "Hata: ${exception.message}", Toast.LENGTH_SHORT)
                    .show()
            })
        }
    }
}