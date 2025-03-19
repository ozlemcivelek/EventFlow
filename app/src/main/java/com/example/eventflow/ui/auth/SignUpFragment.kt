package com.example.eventflow.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eventflow.database.repository.Resource
import com.example.eventflow.databinding.FragmentSignUpBinding
import com.example.eventflow.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpButton.setOnClickListener {
            //zaten mevcut ise login ekranına git veya uyarı ver.
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val passwordAgain = binding.passwordAgainEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            //burada again password kontrolü yap
            viewModel.signUp(name, email, password)

            viewModel.signupFlow.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Error -> {
                        (activity as MainActivity).hideProgress()
                        Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    Resource.Loading -> {
                        (activity as MainActivity).showProgress()
                    }

                    is Resource.Success -> {
                        (activity as MainActivity).hideProgress()
                        findNavController().navigate(
                            SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
                        )
                    }
                    null -> TODO()
                }

            }
        }

        binding.loginTextView.setOnClickListener {
            findNavController().navigate(
                SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
            )
        }
    }

}