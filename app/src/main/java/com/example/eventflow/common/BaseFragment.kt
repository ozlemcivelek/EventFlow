package com.example.eventflow.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.eventflow.ui.MainActivity

abstract class BaseFragment<VM : BaseViewModel>() : Fragment() {

    abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Loading durumu dinlenir
        observeLoadingState()
    }

    private fun observeLoadingState() {
        viewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                (requireActivity() as MainActivity).showProgress()
            } else {
                (requireActivity() as MainActivity).hideProgress()
            }
        }
    }
}
