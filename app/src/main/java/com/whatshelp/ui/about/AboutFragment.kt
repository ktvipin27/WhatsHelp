package com.whatshelp.ui.about

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.whatshelp.BuildConfig
import com.whatshelp.R
import com.whatshelp.databinding.FragmentAboutBinding
import com.whatshelp.manager.analytics.AnalyticsEvent
import com.whatshelp.ui.base.VBFragment
import com.whatshelp.util.Constants.URL_DEVELOPER_PROFILE
import com.whatshelp.util.URLSpanWithListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class AboutFragment : VBFragment<FragmentAboutBinding, AboutViewModel>() {

    override val viewModel: AboutViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentAboutBinding = FragmentAboutBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentYear = Calendar.getInstance().get(Calendar.YEAR);
        val string =
            SpannableString(getString(R.string.app_copyright, currentYear, currentYear + 1))
        string.setSpan(URLSpanWithListener(URL_DEVELOPER_PROFILE) {
            analyticsManager.logEvent(AnalyticsEvent.About.DeveloperConnect)
        }, string.length - 8, string.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.run {

            tvCopyRight.text = string
            tvCopyRight.movementMethod = LinkMovementMethod.getInstance()
            tvAppVersion.text =
                getString(R.string.app_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
        }
    }
}