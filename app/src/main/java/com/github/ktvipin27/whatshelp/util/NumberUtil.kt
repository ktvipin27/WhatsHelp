package com.github.ktvipin27.whatshelp.util

import android.content.Context
import android.telephony.PhoneNumberUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vipin KT on 16/10/21
 */
@Singleton
class NumberUtil @Inject constructor(@ApplicationContext val context: Context) {

    sealed class NumberValidity {
        data class ValidNumber(val code: Int, val number: String) : NumberValidity()
        data class InvalidCountryCode(val number: String) : NumberValidity()
        object InvalidNumber : NumberValidity()
    }

    private val phoneUtil: PhoneNumberUtil = PhoneNumberUtil.createInstance(context)

    fun isValidFullNumber(number: String): NumberValidity {
        return try {

            val spaceRemovedNumber = number.replace(" ", "")
            val isGlobalPhoneNumber = PhoneNumberUtils.isGlobalPhoneNumber(spaceRemovedNumber)
            if (isGlobalPhoneNumber) {
                val numberProto = phoneUtil.parse(number, "")
                NumberValidity.ValidNumber(numberProto.countryCode,
                    numberProto.nationalNumber.toString())
            } else
                NumberValidity.InvalidNumber

        } catch (e: NumberParseException) {
            if (e.errorType == NumberParseException.ErrorType.INVALID_COUNTRY_CODE)
                NumberValidity.InvalidCountryCode(number)
            else
                NumberValidity.InvalidNumber
        }
    }
}