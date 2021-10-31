package com.whatshelp.ui.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

/**
 * Created by Vipin KT on 31/10/21
 */
abstract class BaseFragment<VM : ViewModel> : Fragment() {
    protected abstract val viewModel: VM
}