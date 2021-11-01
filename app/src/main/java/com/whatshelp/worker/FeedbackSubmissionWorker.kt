package com.whatshelp.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.whatshelp.data.db.dao.MessageDao
import com.whatshelp.data.model.Feedback
import com.whatshelp.util.complete
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


/**
 * Created by Vipin KT on 26/10/21
 */
@HiltWorker
class FeedbackSubmissionWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val messageDao: MessageDao,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val database = Firebase.database
        val myRef = database.getReference(FIREBASE_FEEDBACK_NODE)

        val isCompleted = myRef
            .push()
            .setValue(getFeedback())
            .complete()

        return if (isCompleted) Result.success() else Result.failure()
    }

    private fun getFeedback(): Feedback = inputData.getString(KEY_FEEDBACK)?.let {
        return Gson().fromJson(it, Feedback::class.java)
    } ?: throw IllegalStateException("$KEY_FEEDBACK should be provided as input data.")

    companion object {
        const val FIREBASE_FEEDBACK_NODE = "feedback"
        const val KEY_FEEDBACK = "feedback"
    }
}