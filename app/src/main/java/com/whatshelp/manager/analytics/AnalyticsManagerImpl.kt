package com.whatshelp.manager.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Vipin KT on 02/11/21
 */
@Singleton
class AnalyticsManagerImpl @Inject constructor(
    @ApplicationContext val context: Context,
    private val firebaseAnalytics: FirebaseAnalytics,
) : AnalyticsManager {

    override fun logScreenView(screenName: String) =
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
        }

    override fun logEvent(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.event) {
            event.params.forEach {
                param(it.first, it.second)
            }
        }
    }

}