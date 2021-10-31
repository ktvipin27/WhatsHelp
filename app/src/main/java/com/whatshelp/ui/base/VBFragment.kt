package com.whatshelp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

/**
 * Created by Vipin KT on 31/10/21
 */
abstract class VBFragment<VB : ViewBinding, VM : ViewModel> : BaseFragment<VM>() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
}