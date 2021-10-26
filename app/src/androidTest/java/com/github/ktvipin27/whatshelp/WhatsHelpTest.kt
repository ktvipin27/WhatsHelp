package com.github.ktvipin27.whatshelp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class WhatsHelpTest {

    @Test
    fun varifyPackageNameIsCorrect() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val expectedPackageName = if (BuildConfig.DEBUG)
            "com.github.ktvipin27.whatshelp.debug"
        else
            "com.github.ktvipin27.whatshelp"
        assertEquals(expectedPackageName, appContext.packageName)
    }
}