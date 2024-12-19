package com.example.eventflow.ui.service

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.eventflow.common.BaseFragment
import com.example.eventflow.databinding.FragmentServiceDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class ServiceDetailFragment : BaseFragment<ServiceViewModel>() {

    private var _binding: FragmentServiceDetailBinding? = null
    private val binding get() = _binding!!

    override val viewModel: ServiceViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentServiceDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedItem.observe(viewLifecycleOwner) { service ->
            service ?: return@observe
            binding.pageTitle.isVisible = false
            binding.pageUpdateTitle.isVisible = true
            binding.serviceTitleEditText.setText(service.serviceName)
            binding.serviceDescriptionEditText.setText(service.serviceDescription)
            binding.servicePriceEditText.setText(service.servicePrice)
            binding.serviceUpdateButton.isVisible = true
            binding.serviceSaveButton.isVisible = false
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.serviceTitleEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setTitle(text.toString())
        }

        binding.serviceDescriptionEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setDescription(text.toString())
        }

        binding.servicePriceEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setPrice(text.toString())
        }

        binding.serviceSaveButton.setOnClickListener {
            //TODO: Null ve empty kontrollerini yap
            if (!viewModel.isDataValid()) {
                Toast.makeText(
                    requireContext(), "Eksik alanlar var, kontrol et", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            viewModel.saveServices()
            findNavController().popBackStack()
        }

        binding.serviceUpdateButton.setOnClickListener {
            viewModel.updateService(viewModel.service)
            actionServiceDetailFragmentToServiceFragment()
        }
    }

    private fun actionServiceDetailFragmentToServiceFragment(){
        val action =
            ServiceDetailFragmentDirections.actionServiceDetailFragmentToServiceFragment()
        findNavController().navigate(action)
    }
}