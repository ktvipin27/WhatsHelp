package com.whatshelp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.whatshelp.BR

/**
 * Created by Vipin KT on 31/10/21
 */
abstract class DBFragment<DB : ViewDataBinding, VM : ViewModel> : BaseFragment<VM>() {

    private lateinit var _binding: DB
    protected val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false)
        _binding.run {
            setVariable(BR.viewModel, viewModel)
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding.unbind()
        super.onDestroyView()
    }

    @LayoutRes
    protected abstract fun getLayoutResource(): Int
}