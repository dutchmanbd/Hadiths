package com.ticonsys.hadiths.ui.fragments.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding


abstract class BaseFragment<VB : ViewBinding, VM : ViewModel>(
    @LayoutRes contentLayoutId: Int
) : Fragment(contentLayoutId) {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    protected abstract val viewModel: VM


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = initializeViewBinding(view)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected abstract fun initializeViewBinding(view: View): VB
}