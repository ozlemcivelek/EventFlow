package com.example.eventflow.ui.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eventflow.R
import com.example.eventflow.common.BaseFragment
import com.example.eventflow.databinding.FragmentEventDetailBinding
import com.example.eventflow.models.CustomerModel
import com.example.eventflow.ui.SharedViewModel
import com.example.eventflow.ui.customer.CustomerEditBottomSheet
import com.example.eventflow.ui.customer.CustomerListBottomSheet
import com.example.eventflow.ui.service.ServiceViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import kotlin.getValue

@AndroidEntryPoint
class EventDetailFragment : BaseFragment<EventDetailViewModel>() {
    override val viewModel: EventDetailViewModel by viewModels()

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private val serviceViewModel by viewModels<ServiceViewModel>()

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

        addChips()
        binding.serviceAddTextView.setOnClickListener {
            findNavController().navigate(R.id.serviceDetailFragment)
        }

        binding.eventDescriptionEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setDescription(text.toString())
        }

        binding.radioCelebration.isChecked = true
        viewModel.setCategory("Kutlama")
        binding.categoryRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.radioCelebration.id -> viewModel.setCategory("Kutlama")
                binding.radioWorkshop.id -> viewModel.setCategory("Atölye")
            }
        }

        binding.eventDateEditText.doOnTextChanged { text, _, _, _ ->
            val input = text.toString()?.replace("/", "") ?: ""
            if (input.length <= 8) {
                val formatted = when {
                    input.length >= 5 -> "${input.substring(0, 2)}/${
                        input.substring(
                            2,
                            4
                        )
                    }/${input.substring(4)}"

                    input.length >= 3 -> "${input.substring(0, 2)}/${input.substring(2)}"
                    input.isNotEmpty() -> input
                    else -> ""
                }
                if (formatted != text.toString()) {
                    binding.eventDateEditText.setText(formatted)
                    binding.eventDateEditText.setSelection(formatted.length)
                }
            }

            viewModel.setDate(text.toString())
        }

        viewModel.eventDate.observe(viewLifecycleOwner) { date ->
            binding.eventDateEditText.setText(date)
        }

        binding.eventDate.setEndIconOnClickListener {
            DatePickerDialog(requireContext()).apply {
                setOnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate =
                        String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                    viewModel.setEventDate(formattedDate)
                }
                updateDate(
                    viewModel.calendar.get(Calendar.YEAR),
                    viewModel.calendar.get(Calendar.MONTH),
                    viewModel.calendar.get(Calendar.DAY_OF_MONTH)
                )
            }.show()
        }

        binding.eventStartTimeEditText.doOnTextChanged { text, _, _, _ ->
            timeFormat(text.toString(), binding.eventStartTimeEditText)
            viewModel.setStartTime(text.toString())
        }

        binding.eventEndTimeEditText.doOnTextChanged { text, _, _, _ ->
            timeFormat(text.toString(), binding.eventEndTimeEditText)
            viewModel.setEndTime(text.toString())
        }

        binding.eventLocationEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setLocation(text.toString())
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
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
            if (!viewModel.isDataEventValid()) {
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

        viewModel.setCalendarTime(sharedViewModel.calendarTime)

    }

    fun addChips(chipGroup: ChipGroup = binding.chipGroup) {
        serviceViewModel.getServices()
        serviceViewModel.serviceModel.observe(viewLifecycleOwner) {
            isVisibleChip(it.isEmpty())
            it.forEach { service ->
                val chip = Chip(requireContext()).apply {
                    text = service.serviceName
                    chipBackgroundColor = ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.chip_background
                    )
                    setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.chip_text_color
                        )
                    )
                    isCheckable = true
                }

                chip.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        Toast.makeText(
                            requireContext(),
                            "Seçildi: ${service.serviceName}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Kaldırıldı: ${service.serviceName}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    viewModel.setServices(getSelectedChips())
                }

                chipGroup.addView(chip)
            }
        }
    }

    private fun isVisibleChip(isEmpty: Boolean){
        if(isEmpty){
            binding.chipGroupScrollView.isVisible = false
            binding.serviceTitle.isVisible = false
            binding.serviceAddTextView.isVisible = true
        }
    }

    private fun getSelectedChips(chipGroup: ChipGroup = binding.chipGroup): List<String> {
        val selectedChips = mutableListOf<String>()
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedChips.add(chip.text.toString())
            }
        }
        return selectedChips
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

    private fun timeFormat(time: String, editText: EditText) {
        val input = time?.replace(":", "") ?: ""
        if (input.length <= 4) {
            val formatted = when {
                input.length >= 3 -> "${input.substring(0, 2)}:${input.substring(2)}"
                input.length >= 2 -> "${input.substring(0, 2)}:"
                else -> input
            }
            if (formatted != time) {
                editText.setText(formatted)
                editText.setSelection(formatted.length)
            }
        }
    }

}
