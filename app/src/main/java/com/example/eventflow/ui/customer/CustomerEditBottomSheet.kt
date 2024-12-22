package com.example.eventflow.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.example.eventflow.databinding.BottomSheetCustomerEditBinding
import com.example.eventflow.models.CustomerModel
import com.example.eventflow.ui.event.AddEventViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class CustomerEditBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetCustomerEditBinding? = null
    private val binding get() = _binding!!

    var onSaveClicked: ((CustomerModel) -> Unit)? = null // Save callback

    private val viewModel by viewModels<AddEventViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetCustomerEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nameEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setCustomerName(text.toString())
        }

        binding.emailEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setCustomerEmail(text.toString())
        }

        binding.phoneEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setCustomerPhone(text.toString())
        }

        arguments?.let {
            val name = it.getString("name") ?: ""
            val email = it.getString("email") ?: ""
            val phone = it.getString("phone") ?: ""

            binding.nameEditText.setText(name)
            binding.emailEditText.setText(email)
            binding.phoneEditText.setText(phone)
        }

        binding.saveButton.setOnClickListener {
            if (!viewModel.isDataCustomerValid()) {
                Toast.makeText(
                    requireContext(), "Eksik alanlar var, kontrol et", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val updatedName = binding.nameEditText.text.toString()
            val updatedEmail = binding.emailEditText.text.toString()
            val updatedPhone = binding.phoneEditText.text.toString()

            onSaveClicked?.invoke(
                CustomerModel(
                    name = updatedName,
                    email = updatedEmail,
                    phone = updatedPhone
                )
            )
            dismiss() // Bottom Sheet'i kapat
        }

        // İptal butonuna tıklanınca kapat
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
            name: String = "", email: String = "", phone: String = ""
        ): CustomerEditBottomSheet {
            val fragment = CustomerEditBottomSheet()
            val args = Bundle()
            args.putString("name", name)
            args.putString("email", email)
            args.putString("phone", phone)
            fragment.arguments = args
            return fragment
        }
    }
}

