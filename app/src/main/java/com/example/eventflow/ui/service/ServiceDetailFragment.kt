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
import com.example.eventflow.databinding.FragmentServiceDetailBinding
import kotlin.getValue

class ServiceDetailFragment : Fragment() {

    private var _binding: FragmentServiceDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<ServiceViewModel>()

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

        //TODO: Fotoğraf ekleme için, bottom sheet -> galeriden seçme veya kamera açma kısmı eklenecek.

        binding.serviceSaveButton.setOnClickListener {
            //TODO: Null ve empty kontrollerini yap
            if (!viewModel.isDataValid()) {
                Toast.makeText(
                    requireContext(), "Eksik alanlar var, kontrol et", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            viewModel.saveServices(onSuccess = {
                //actionServiceDetailFragmentToServiceFragment()
                findNavController().popBackStack()
                Toast.makeText(requireContext(), "Hizmet kaydedildi", Toast.LENGTH_SHORT).show()
            }, onFailure = { exception ->
                Toast.makeText(requireContext(), "Hata: ${exception.message}", Toast.LENGTH_SHORT)
                    .show()
            })
        }

        binding.serviceUpdateButton.setOnClickListener {
            viewModel.updateService(
                viewModel.service,
                onSuccess = {
                actionServiceDetailFragmentToServiceFragment()
                Toast.makeText(requireContext(), "Hizmet güncellendi", Toast.LENGTH_SHORT).show()
                }, onFailure = { exception ->
                    Toast.makeText(requireContext(), "Hizmet güncellemede hata oluştu: ${exception.message}", Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun actionServiceDetailFragmentToServiceFragment(){
        val action =
            ServiceDetailFragmentDirections.actionServiceDetailFragmentToServiceFragment()
        findNavController().navigate(action)
    }
}