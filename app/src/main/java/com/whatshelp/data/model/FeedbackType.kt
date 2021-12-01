package com.whatshelp.data.model

import com.whatshelp.R

/**
 * Created by Vipin KT on 01/11/21
 */
enum class FeedbackType(val stringRes: Int) {
    SUGGESTION(R.string.label_feedback_type_suggestion),
    FEATURE_REQUEST(R.string.label_feedback_feature_request),
    BUG(R.string.label_feedback_type_bug)
}