package com.github.ktvipin27.whatshelp.util

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

    suspend fun isValidFullNumber(number: String): NumberValidity {
        return try {
            val numberProto = phoneUtil.parse(number, "")
            Log.d("isValidFullNumber","Country code: " + numberProto.countryCode)
            NumberValidity.ValidNumber(numberProto.countryCode,
                numberProto.nationalNumber.toString())
        } catch (e: NumberParseException) {
            Log.d("isValidFullNumber", "NumberParseException was thrown: $e")
            if (e.errorType == NumberParseException.ErrorType.INVALID_COUNTRY_CODE)
                NumberValidity.InvalidCountryCode(number)
            else
                NumberValidity.InvalidNumber
        }
    }
}