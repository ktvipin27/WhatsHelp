package com.whatshelp.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Created by Vipin KT on 26/10/21
 */
@Entity(tableName = "messages")
@Parcelize
data class Message(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val text: String,

    var timeStamp: Long =  System.currentTimeMillis()
) : Parcelable