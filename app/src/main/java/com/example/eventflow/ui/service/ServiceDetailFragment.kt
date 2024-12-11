package com.example.eventflow.ui.service

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eventflow.databinding.FragmentServiceDetailBinding
import kotlin.getValue

class ServiceDetailFragment : Fragment() {

    private var _binding: FragmentServiceDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ServiceViewModel>()

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
                val action = ServiceDetailFragmentDirections.actionServiceDetailFragmentToServiceFragment()
                findNavController().navigate(action)
                Toast.makeText(requireContext(), "Hizmet kaydedildi", Toast.LENGTH_SHORT).show()
            }, onFailure = { exception ->
                Toast.makeText(requireContext(), "Hata: ${exception.message}", Toast.LENGTH_SHORT)
                    .show()
            })
        }
    }
}