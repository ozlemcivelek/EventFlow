package com.example.eventflow.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.eventflow.databinding.BottomSheetChangePasswordBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChangePasswordBottomSheet() : BottomSheetDialogFragment() {

    private var _binding: BottomSheetChangePasswordBinding? = null
    private val binding get() = _binding!!

    var onChangePasswordClicked: (String) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.updatePasswordButton.setOnClickListener {
            onChangePasswordClicked.invoke(
                binding.newPasswordTextInputLayout.editText?.text.toString()
            )
            dismiss()
        }

    }
}