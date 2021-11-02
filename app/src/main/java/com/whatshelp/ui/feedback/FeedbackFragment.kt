package com.whatshelp.ui.feedback

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.whatshelp.R
import com.whatshelp.databinding.FragmentFeedbackBinding
import com.whatshelp.manager.analytics.AnalyticsEvent
import com.whatshelp.ui.base.DBFragment
import com.whatshelp.util.hideKeyboard
import com.whatshelp.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedbackFragment : DBFragment<FragmentFeedbackBinding, FeedbackViewModel>() {

    override val viewModel: FeedbackViewModel by viewModels()

    override fun getLayoutResource(): Int = R.layout.fragment_feedback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rgFeedbackType.setOnCheckedChangeListener { _, _ ->
            analyticsManager.logEvent(AnalyticsEvent.Feedback.Change)
        }

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