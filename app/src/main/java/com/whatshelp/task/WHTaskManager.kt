package com.whatshelp.task

import com.whatshelp.data.model.FeedbackType

/**
 * Created by Vipin KT on 02/11/21
 */
interface WHTaskManager {
    fun submitFeedback(feedbackType: FeedbackType, feedbackText: String)
}