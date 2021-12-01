package com.whatshelp.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.whatshelp.manager.analytics.AnalyticsEvent
import com.whatshelp.manager.app.AppManager
import com.whatshelp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AboutFragment : BaseFragment<AboutViewModel>() {

    override val viewModel: AboutViewModel by viewModels()

    @Inject
    lateinit var appManager: AppManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            AboutScreen {
                analyticsManager.logEvent(AnalyticsEvent.About.DeveloperConnect)
                appManager.openApplication(it)
            }
        }
    }
}