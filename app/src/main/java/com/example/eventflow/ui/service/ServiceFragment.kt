package com.example.eventflow.ui.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.eventflow.adapter.ServiceAdapter
import com.example.eventflow.common.BaseFragment
import com.example.eventflow.databinding.FragmentServiceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServiceFragment() : BaseFragment<ServiceViewModel>() {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!

    override val viewModel: ServiceViewModel by activityViewModels()

    private val serviceAdapter = ServiceAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.serviceListRecyclerView.adapter = serviceAdapter

        viewModel.selectItem(null)
        viewModel.serviceModel.observe(viewLifecycleOwner) {
            serviceAdapter.setItems(it)
        }
        viewModel.getServices()

        serviceAdapter.onItemClicked = {
            //Editte de aynı seyler var gibi yapılmalı mı?
        }
        serviceAdapter.onDeleteClicked = { service ->
            viewModel.deleteService(service.serviceId ?: "")
            serviceAdapter.removeItem(service)
        }
        serviceAdapter.onEditClicked = { service ->
            viewModel.selectItem(service)
            val action = ServiceFragmentDirections.actionServiceFragmentToServiceDetailFragment()
            findNavController().navigate(action)
        }
    }

}