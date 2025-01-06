package com.example.eventflow.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eventflow.database.repository.Resource
import com.example.eventflow.databinding.FragmentProfileBinding
import com.example.eventflow.ui.MainActivity
import com.example.eventflow.ui.auth.AuthViewModel
import com.example.eventflow.utils.showKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!


    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentUser?.displayName?.let {
            binding.profileNameEditText.setText(it)
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.checkboxError.visibility = View.GONE
            }
        }

        binding.profileUpdateButton.setOnClickListener {
            if (binding.checkbox.isChecked) {
                binding.checkboxError.visibility = View.GONE

                val name = binding.profileNameEditText.text.toString()
                viewModel.updateProfile(name)
                viewModel.updateFlow.observe(viewLifecycleOwner) {
                    when (it) {
                        is Resource.Error -> {
                            (activity as MainActivity).hideProgress()
                            Toast.makeText(
                                requireContext(),
                                it.exception.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        Resource.Loading -> {
                            (activity as MainActivity).showProgress()
                        }

                        is Resource.Success -> {
                            findNavController().popBackStack()
                            (activity as MainActivity).hideProgress()
                        }

                        null -> TODO()
                    }
                }
            } else
                binding.checkboxError.visibility = View.VISIBLE
        }

        showKeyboard(binding.profileNameEditText)
    }
}