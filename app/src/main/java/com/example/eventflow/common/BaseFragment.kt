package com.example.eventflow.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.eventflow.ui.MainActivity

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    protected lateinit var viewModel: VM

    abstract val viewModelClass: Class<VM>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel'i initialize et
        viewModel = ViewModelProvider(this)[viewModelClass]

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
