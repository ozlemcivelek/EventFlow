package com.example.eventflow.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eventflow.database.repository.Resource
import com.example.eventflow.databinding.FragmentAccountBinding
import com.example.eventflow.ui.MainActivity
import com.example.eventflow.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentUser?.let {
            binding.profileLinearLayout.isVisible = true
            binding.profileEmailTextView.text = it.email.toString()
            binding.profileCompleteCardView.isVisible = false
            binding.profileNameTextView.text = it.displayName.toString()
        }

        binding.changePasswordCardView.setOnClickListener {
            ChangePasswordBottomSheet().apply {
                onChangePasswordClicked = { currentPassword, newPassword ->
                    viewModel.changePassword(currentPassword, newPassword)
                }
            }.show(childFragmentManager, "ChangePasswordBottomSheet")
        }
        viewModel.changePasswordFlow.observe(viewLifecycleOwner) {
            if (it is Resource.Loading)
                (activity as MainActivity).showProgress()
            else
                (activity as MainActivity).hideProgress()

            when (it) {
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT)
                        .show()
                }

                is Resource.Success -> {
                    Toast.makeText(requireContext(), it.result.toString(), Toast.LENGTH_LONG).show()
                    viewModel.logout()
                }

                else -> Unit
            }
        }

        binding.profileLinearLayout.setOnClickListener {
            findNavController().navigate(
                AccountFragmentDirections.actionAccountFragmentToProfileFragment()
            )
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.logoutStatus.observe(viewLifecycleOwner) {
            if (it) {
               findNavController().navigate(
                    AccountFragmentDirections.actionAccountFragmentToLoginFragment()
                )
            }
        }

        binding.logoutCardView.setOnClickListener {
            viewModel.logout()
        }
    }
}