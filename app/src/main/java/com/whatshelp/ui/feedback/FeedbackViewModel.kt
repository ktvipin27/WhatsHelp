package com.whatshelp.ui.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whatshelp.data.model.FeedbackType
import com.whatshelp.manager.analytics.AnalyticsEvent
import com.whatshelp.manager.analytics.AnalyticsManager
import com.whatshelp.manager.task.TaskManager
import com.whatshelp.util.SingleLiveEvent
import com.whatshelp.util.addSources
import com.whatshelp.util.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val taskManager: TaskManager,
    private val analyticsManager: AnalyticsManager
) : ViewModel() {
    val state = SingleLiveEvent<FeedbackState>()

    private val _feedbackType = MutableLiveData<FeedbackType>()
    val feedbackType: LiveData<FeedbackType>
        get() = _feedbackType
    private val _feedback = MutableLiveData<String>()
    val feedback: LiveData<String>
        get() = _feedback
    private val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    val enableSubmission: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSources(feedbackType, feedback, email) {
            value = isEnteredDataValid(feedbackType.value, feedback.value, email.value)
        }
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
        taskManager.submitFeedback(feedbackType.value!!, feedback.value!!, email.value!!)
        state.value = FeedbackState.Submitted
    }
}