package com.whatshelp.ui.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.whatshelp.R
import com.whatshelp.manager.analytics.AnalyticsEvent
import com.whatshelp.ui.base.BaseFragment
import com.whatshelp.util.hideKeyboard
import com.whatshelp.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedbackFragment : BaseFragment<FeedbackViewModel>() {

    override val viewModel: FeedbackViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            FeedbackScreen()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                FeedbackState.Submitted -> {
                    view.hideKeyboard()
                    analyticsManager.logEvent(AnalyticsEvent.Feedback.Submit)
                    toast(R.string.message_feedback_submit_success)
                    findNavController().navigateUp()
                }
            }
        })
    }

}