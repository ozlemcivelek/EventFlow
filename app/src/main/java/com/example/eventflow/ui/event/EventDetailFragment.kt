package com.example.eventflow.ui.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eventflow.databinding.FragmentEventDetailBinding
import com.example.eventflow.models.CustomerModel
import com.example.eventflow.ui.customer.CustomerEditBottomSheet
import com.example.eventflow.ui.customer.CustomerListBottomSheet
import java.util.Calendar
import kotlin.getValue

class EventDetailFragment : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<EventDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFirebaseCustomers(onSuccess = {
            if (it.isEmpty()) {
                binding.infoCardView.isVisible = true
                binding.customerProfileCardView.isVisible = false
                binding.emptyCustomerCardView.isVisible = false
            } else {
                binding.emptyCustomerCardView.isVisible = true
                binding.infoCardView.isVisible = false
                binding.customerProfileCardView.isVisible = false
            }
        }, onFailure = {
            Toast.makeText(requireContext(), "Hata: ${it.message}", Toast.LENGTH_SHORT).show()
        })

        binding.eventTitleEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setTitle(text.toString())
        }

        binding.eventDescriptionEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setDescription(text.toString())
        }

        binding.radioCelebration.isChecked = true
        binding.categoryRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.radioCelebration.id -> viewModel.setCategory("Kutlama")
                binding.radioWorkshop.id -> viewModel.setCategory("Atölye")
            }
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

        viewModel.eventDate.observe(viewLifecycleOwner) { date ->
            binding.eventDateEditText.setText(date)
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.eventDate.setEndIconOnClickListener {
            DatePickerDialog(requireContext()).apply {
                setOnDateSetListener() { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate =
                        String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                    viewModel.setEventDate(formattedDate)
                }
            }.show()
        }

        binding.eventStartTime.setEndIconOnClickListener {
            showTimePicker { selectedTime ->
                binding.eventStartTimeEditText.setText(selectedTime)
            }
        }

        binding.eventEndTime.setEndIconOnClickListener {
            showTimePicker { selectedTime ->
                binding.eventEndTimeEditText.setText(selectedTime)
            }
        }

        binding.eventSaveButton.setOnClickListener {
            // TODO: Null ve empty kontrollerini yap
            if (!viewModel.isDataValid()) {
                Toast.makeText(
                    requireContext(), "Eksik alanlar var, kontrol et", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            viewModel.saveEvent(onSuccess = {
                val action = EventDetailFragmentDirections.actionEventDetailFragmentToHomeFragment()
                findNavController().navigate(action)
                Toast.makeText(requireContext(), "Etkinlik kaydedildi", Toast.LENGTH_SHORT).show()
            }, onFailure = { exception ->
                Toast.makeText(requireContext(), "Hata: ${exception.message}", Toast.LENGTH_SHORT)
                    .show()
            })
        }

        binding.customDescriptionButton.setOnClickListener {
            binding.eventDescription.visibility = View.VISIBLE
            binding.customDescriptionButton.visibility = View.GONE
        }

        binding.customerAddTextView.setOnClickListener {
            customerEditBottomSheet()
        }

        binding.customerProfileCardView.setOnClickListener {
            customerListBottomSheet()
        }

        binding.emptyCustomerCardView.setOnClickListener {
            customerListBottomSheet()
        }

        viewModel.selectedCustomer.observe(viewLifecycleOwner) {
            customerCardView(it)
        }
    }

    private fun customerEditBottomSheet() {
        val bottomSheet = CustomerEditBottomSheet.Companion.newInstance()
        bottomSheet.onSaveClicked = { customer ->
            viewModel.saveCustomers(customer, onSuccess = {

            }, onFailure = { exception ->
                Toast.makeText(
                    requireContext(), "Hata: ${exception.message}", Toast.LENGTH_SHORT
                ).show()
            })

        }

        bottomSheet.show(parentFragmentManager, "CustomerEditBottomSheet")
    }

    private fun customerListBottomSheet() {
        CustomerListBottomSheet(
            customerList = viewModel.customers,
            onItemClick = { selectedCustomer ->
                viewModel.selectCustomer(selectedCustomer)
            }
        ).show(parentFragmentManager, "CustomerListBottomSheet")
    }

    private fun customerCardView(customer: CustomerModel) {
        binding.customerProfileCardView.visibility = View.VISIBLE
        binding.emptyCustomerCardView.isVisible = false
        binding.infoCardView.isVisible = false
        binding.customerEditButton.visibility = View.GONE
        binding.customerNameTextView.text = customer.name
        binding.emailTextView.text = customer.email
        binding.phoneTextView.text = customer.phone
    }

    private fun showTimePicker(onTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            onTimeSelected(formattedTime)
        }, hour, minute, true).show()
    }

}
