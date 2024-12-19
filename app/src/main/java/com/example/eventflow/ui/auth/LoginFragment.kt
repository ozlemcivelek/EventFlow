package com.example.eventflow.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eventflow.database.repository.Resource
import com.example.eventflow.databinding.FragmentLoginBinding
import com.example.eventflow.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginTextView.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.toSignUp())
        }

        binding.loginButton.setOnClickListener {
            viewModel.login(
                binding.emailAddressEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )

            viewModel.loginFlow.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Error -> {
                        //HATA mesajina göre ekranda işlem yapma
                        (activity as MainActivity).hideProgress()
                        Log.d("TAG", "HATA MESAJI: ${it.exception.message} ")
                        Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    Resource.Loading -> {
                        //TODO show loading
                        (activity as MainActivity).showProgress()
                        //Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Success -> {
                        (activity as MainActivity).hideProgress()
                        findNavController().navigate(LoginFragmentDirections.toHome())
                        //Toast.makeText(requireContext(), it.result.toString(), Toast.LENGTH_SHORT).show()
                    }

                    null -> TODO()
                }
            }
        }

        if (viewModel.currentUser != null) {
            findNavController().navigate(LoginFragmentDirections.toHome())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}