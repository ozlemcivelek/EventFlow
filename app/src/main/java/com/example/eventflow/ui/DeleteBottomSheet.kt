package com.example.eventflow.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.eventflow.databinding.BottomSheetDeleteBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeleteBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetDeleteBinding? = null
    private val binding get() = _binding!!

    var onDeleteClicked: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetDeleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(DESCRIPTION_ARG)?.let {
            binding.subTitleTextView.text = it
        }


        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        binding.deleteButton.setOnClickListener {
            onDeleteClicked?.invoke()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DESCRIPTION_ARG = "Description"

        fun newInstance(description: String): DeleteBottomSheet {
            return DeleteBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(DESCRIPTION_ARG, description)
                }
            }
        }
    }
}