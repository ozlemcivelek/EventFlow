package com.example.eventflow.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.eventflow.adapter.CustomerAdapter
import com.example.eventflow.databinding.BottomSheetCustomerListBinding
import com.example.eventflow.models.CustomerModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CustomerListBottomSheet(
    private val customerList: List<CustomerModel>,
    private val onItemClick: (CustomerModel) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetCustomerListBinding? = null
    private val binding get() = _binding!!

    private val customerAdapter = CustomerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetCustomerListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.customerListRecyclerView.adapter = customerAdapter
        customerAdapter.setItems(customerList)
        customerAdapter.onItemClicked = {
            onItemClick(it)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

