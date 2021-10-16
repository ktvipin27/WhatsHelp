package com.github.ktvipin27.whatshelp.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Created by Vipin KT on 15/10/21
 */
@Entity
@Parcelize
data class History(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val code: String,

    val number: String,

    val formattedFullNumber: String
) : Parcelable