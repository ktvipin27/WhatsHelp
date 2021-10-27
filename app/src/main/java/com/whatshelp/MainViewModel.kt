package com.whatshelp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whatshelp.data.model.WhatsAppNumber
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
) : ViewModel() {

    private var lastCopiedNumber :WhatsAppNumber?  = null
    val copiedNumberLiveData: SingleLiveEvent<WhatsAppNumber?> = SingleLiveEvent()

    init {
        copiedNumberLiveData.value = null
    }

    fun updateCopiedText(text: String) {
        viewModelScope.launch {
            val waNumber = when (val numberValidity = numberUtil.isValidFullNumber(text)) {
                is NumberUtil.NumberValidity.ValidNumber ->
                    WhatsAppNumber(numberValidity.code, numberValidity.number)
                is NumberUtil.NumberValidity.InvalidCountryCode ->
                    WhatsAppNumber(number = numberValidity.number)
                NumberUtil.NumberValidity.InvalidNumber ->
                    null
            }

            if (waNumber != lastCopiedNumber) {
                lastCopiedNumber = waNumber
                copiedNumberLiveData.value = lastCopiedNumber
            }
        }
    }

    fun resetCopiedNumber(){
        copiedNumberLiveData.value = null
    }
}