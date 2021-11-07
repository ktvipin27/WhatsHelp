package com.whatshelp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whatshelp.data.model.WhatsAppNumber
import com.whatshelp.manager.pref.PreferencesManager
import com.whatshelp.util.NumberUtil
import com.whatshelp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Vipin KT on 21/10/21
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val numberUtil: NumberUtil,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    val copiedNumberLiveData: SingleLiveEvent<WhatsAppNumber?> = SingleLiveEvent()

    init {
        copiedNumberLiveData.value = null
    }

    fun updateCopiedText(text: String) {
        val lastCopiedNumber = preferencesManager.getCopiedNumber()
        viewModelScope.launch {
            val waNumber = when (val numberValidity = numberUtil.isValidFullNumber(text)) {
                is NumberUtil.NumberValidity.ValidNumber ->
                    WhatsAppNumber(numberValidity.code, numberValidity.number)
                is NumberUtil.NumberValidity.InvalidCountryCode ->
                    WhatsAppNumber(number = numberValidity.number)
                NumberUtil.NumberValidity.InvalidNumber ->
                    null
            }
            val currentTime = System.currentTimeMillis()

            val diff: Long = currentTime - lastCopiedNumber.second
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60
            val days = hours / 24

            waNumber?.let {
                if (waNumber.fullNumber != lastCopiedNumber.first || days > 0) {
                    val pair = Pair(waNumber.fullNumber, currentTime)
                    preferencesManager.setCopiedNumber(pair)
                    copiedNumberLiveData.value = waNumber
                }
            }

        }
    }

    fun resetCopiedNumber() {
        copiedNumberLiveData.value = null
    }
}