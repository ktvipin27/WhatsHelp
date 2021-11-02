package com.whatshelp.manager.task

import com.whatshelp.data.model.FeedbackType

/**
 * Created by Vipin KT on 02/11/21
 */
interface TaskManager {
    fun submitFeedback(feedbackType: FeedbackType, feedbackText: String)
}