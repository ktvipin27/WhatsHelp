package com.whatshelp.ui.about

import android.content.Intent
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.whatshelp.BuildConfig
import com.whatshelp.R
import com.whatshelp.databinding.FragmentAboutBinding
import com.whatshelp.util.Constants.GITHUB_PROFILE_URL
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentYear = Calendar.getInstance().get(Calendar.YEAR);
        val string =
            SpannableString(getString(R.string.app_copyright, currentYear, currentYear + 1))
        string.setSpan(URLSpan(GITHUB_PROFILE_URL), 12, string.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.run {

            tvCopyRight.text = string
            tvCopyRight.movementMethod = LinkMovementMethod.getInstance()
            tvAppVersion.text =
                getString(R.string.app_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
        }
    }

    private fun launchBrowser(url: String) = Intent(Intent.ACTION_VIEW, Uri.parse(url)).also {
        startActivity(it)
    }
}