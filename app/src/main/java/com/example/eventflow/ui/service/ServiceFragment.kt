package com.example.eventflow.ui.service

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eventflow.adapter.ServiceAdapter
import com.example.eventflow.databinding.FragmentServiceBinding
import kotlin.getValue

class ServiceFragment : Fragment() {

    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<ServiceViewModel>()

    val serviceAdapter = ServiceAdapter()

    //TODO: Service edit kısmı da olacak, fiyat , açıklama değiştirmek isterse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        viewModel.getServices(
            onSuccess = {
            serviceAdapter.setItems(it)
        }, onFailure = { exception ->
            Log.e("ServiceFragment", "Error fetching services: ${exception.message}")
        })
        serviceAdapter.onItemClicked = {
            //Her bir hizmete tıkladığında yapılacak işlemler
        }
        serviceAdapter.onDeleteClicked = {service ->
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