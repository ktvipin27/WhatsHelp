package com.whatshelp.ui.feedback

import androidx.lifecycle.ViewModel
import com.whatshelp.data.model.FeedbackType
import com.whatshelp.manager.analytics.AnalyticsEvent
import com.whatshelp.manager.analytics.AnalyticsManager
import com.whatshelp.manager.task.TaskManager
import com.whatshelp.util.SingleLiveEvent
import com.whatshelp.util.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val taskManager: TaskManager,
    private val analyticsManager: AnalyticsManager
) : ViewModel() {
    val state = SingleLiveEvent<FeedbackState>()

    private val _feedbackType = MutableStateFlow<FeedbackType?>(null)
    val feedbackType: StateFlow<FeedbackType?>
        get() = _feedbackType.asStateFlow()

    private val _feedback = MutableStateFlow("")
    val feedback: StateFlow<String>
        get() = _feedback.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String>
        get() = _email.asStateFlow()

    val enableSubmission =
        combine(feedbackType, feedback, email) { feedbackType, feedback, email ->
            isEnteredDataValid(feedbackType, feedback, email)
        }

    private fun isEnteredDataValid(
        feedbackType: FeedbackType?,
        feedback: String?,
        email: String?,
    ): Boolean {
        return if (email.isNullOrEmpty()) !feedback.isNullOrEmpty() && feedbackType != null
        else email.isValidEmail() && !feedback.isNullOrEmpty() && feedbackType != null
    }

    fun setFeedbackType(type: FeedbackType) {
        if (_feedbackType.value != type)
            analyticsManager.logEvent(AnalyticsEvent.Feedback.Change)
        _feedbackType.value = type
    }

    fun setFeedback(text: String) {
        _feedback.value = text
    }

    fun setEmail(text: String) {
        _email.value = text
    }

    fun submitFeedback() {
        taskManager.submitFeedback(feedbackType.value!!, feedback.value, email.value)
        state.value = FeedbackState.Submitted
    }
}