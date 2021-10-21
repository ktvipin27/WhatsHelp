package com.github.ktvipin27.whatshelp.data.db.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.ktvipin27.whatshelp.data.model.WhatsAppNumber
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Created by Vipin KT on 15/10/21
 */
@Entity
@Parcelize
data class History(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @Embedded
    val whatsAppNumber: WhatsAppNumber,

    val formattedNumber: String,

    var timeStamp: Long =  System.currentTimeMillis()
) : Parcelable