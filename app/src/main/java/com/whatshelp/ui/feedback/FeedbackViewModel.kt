package com.whatshelp.ui.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whatshelp.data.model.FeedbackType
import com.whatshelp.manager.task.TaskManager
import com.whatshelp.util.SingleLiveEvent
import com.whatshelp.util.addSources
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val taskManager: TaskManager,
) : ViewModel() {
    val state = SingleLiveEvent<FeedbackState>()

    val feedbackType = MutableLiveData<FeedbackType>()
    val feedback = MutableLiveData<String>()
    val email = MutableLiveData<String>()

    val enableSubmission: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSources(feedbackType, feedback) {
            value = isEnteredDataValid(feedbackType.value, feedback.value)
        }
    }

    private fun isEnteredDataValid(feedbackType: FeedbackType?, feedback: String?): Boolean =
        !feedback.isNullOrEmpty() && feedbackType != null

    fun setFeedbackType(type: FeedbackType) {
        feedbackType.value = type
    }

    fun submitFeedback() {
        taskManager.submitFeedback(feedbackType.value!!, feedback.value!!)
        state.value = FeedbackState.Submitted
    }
}