package com.whatshelp.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.whatshelp.manager.analytics.AnalyticsManager
import javax.inject.Inject

/**
 * Created by Vipin KT on 31/10/21
 */
abstract class BaseFragment<VM : ViewModel> : Fragment() {
    protected abstract val viewModel: VM

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analyticsManager.logScreenView(javaClass.simpleName)
        analyticsManager.logScreenView(javaClass.simpleName)
    }
}