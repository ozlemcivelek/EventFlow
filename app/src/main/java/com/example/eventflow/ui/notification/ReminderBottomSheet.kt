package com.example.eventflow.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.eventflow.databinding.BottomSheetReminderBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ReminderBottomSheet(
    private var reminderTime: Int,
    private val onReminderSelected: (Int) -> Unit,
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetReminderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.eventTitleEditText.setText(reminderTime.toString())

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.doneButton.setOnClickListener {
            val customReminder = binding.eventTitleEditText.editableText.toString().toInt()
            when {
                binding.radioReminderHour.isChecked -> reminderTime =
                    customReminder * 60 //saat olarak hesaplanacak
                binding.radioReminderDay.isChecked -> reminderTime =
                    customReminder * 24 * 60//gün olarak hesaplanacak
                binding.radioReminderWeek.isChecked -> reminderTime =
                    customReminder * 1440 * 7 //hafta olarak hesaplanacak
            }
            onReminderSelected(reminderTime)
            dismiss()
        }
    }

}