package com.whatshelp.manager.task

import android.os.Build
import androidx.work.*
import com.google.gson.Gson
import com.whatshelp.data.model.AppInfo
import com.whatshelp.data.model.DeviceInfo
import com.whatshelp.data.model.Feedback
import com.whatshelp.data.model.FeedbackType
import com.whatshelp.worker.FeedbackSubmissionWorker
import java.text.DateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vipin KT on 02/11/21
 */
@Singleton
class TaskManagerImpl @Inject constructor(
    private val workManager: WorkManager,
) : TaskManager {

    override fun submitFeedback(feedbackType: FeedbackType, feedbackText: String) {

        val time = Calendar.getInstance().time
        val date: String = DateFormat.getDateTimeInstance().format(time)

        val deviceInfo = DeviceInfo(
            Build.BRAND,
            Build.MANUFACTURER,
            Build.MODEL,
            Build.VERSION.SDK_INT,
            Build.VERSION.RELEASE
        )

        val appInfo = AppInfo()

        val feedback = Feedback(feedbackType, feedbackText, time.time, date, appInfo, deviceInfo)

        val work = OneTimeWorkRequestBuilder<FeedbackSubmissionWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setInputData(
                Data.Builder()
                    .putString(FeedbackSubmissionWorker.KEY_FEEDBACK, Gson().toJson(feedback))
                    .build()
            )
            .build()

        workManager.enqueueUniqueWork(
            FEEDBACK_TASK_NAME,
            ExistingWorkPolicy.REPLACE,
            work
        )
    }

    companion object {
        const val FEEDBACK_TASK_NAME = "Task-Feedback-Submit"
    }
}