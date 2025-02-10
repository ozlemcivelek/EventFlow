package com.example.eventflow.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eventflow.R
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
                if (it is Resource.Loading)
                    (activity as MainActivity).showProgress()
                else
                    (activity as MainActivity).hideProgress()
                when (it) {
                    is Resource.Error -> {
                        //HATA mesajina göre ekranda işlem yapma
                        Log.d("TAG", "HATA MESAJI: ${it.exception.message} ")
                        Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is Resource.Success -> {
                        if (findNavController().currentDestination?.id == R.id.loginFragment)
                            findNavController().navigate(LoginFragmentDirections.toHome())
                        //Toast.makeText(requireContext(), it.result.toString(), Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
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