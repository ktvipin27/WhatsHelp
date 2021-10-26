package com.github.ktvipin27.whatshelp.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.ktvipin27.whatshelp.data.db.dao.MessageDao
import com.github.ktvipin27.whatshelp.data.db.entity.Message
import com.google.gson.GsonBuilder
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset


/**
 * Created by Vipin KT on 26/10/21
 */
@HiltWorker
class DatabasePopulatingWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val messageDao: MessageDao
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val messagesJson = loadJSONFromAsset("messages.json")
        if (messagesJson == null)
            return Result.failure()
        else withContext(Dispatchers.IO) {
            populateDatabase(messagesJson)
        }

        return Result.success()
    }

    private suspend fun populateDatabase(messagesJson: String) {
        val gson = GsonBuilder().create()
        val messages = gson.fromJson(messagesJson, Array<Message>::class.java).toList()
        messageDao.insert(messages)
    }

    private fun loadJSONFromAsset(fileName: String): String? {
        val json: String? = try {
            val `is`: InputStream = appContext.assets.open(fileName)
            val size: Int = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}