package com.whatshelp.ui.chat

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.whatshelp.R
import com.whatshelp.data.model.WhatsAppNumber
import com.whatshelp.manager.analytics.AnalyticsEvent
import com.whatshelp.manager.rating.RatingsManager
import com.whatshelp.manager.share.ShareManager
import com.whatshelp.manager.theme.ThemeManager
import com.whatshelp.manager.whatsapp.WhatsAppManager
import com.whatshelp.ui.MainViewModel
import com.whatshelp.ui.base.BaseFragment
import com.whatshelp.util.Constants.EXTRA_MESSAGE
import com.whatshelp.util.Constants.EXTRA_NUMBER
import com.whatshelp.util.NumberUtil
import com.whatshelp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ChatFragment : BaseFragment<ChatViewModel>() {

    override val viewModel: ChatViewModel by viewModels()

    private val mainViewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var numberUtil: NumberUtil

    @Inject
    lateinit var whatsAppManager: WhatsAppManager

    @Inject
    lateinit var ratingsManager: RatingsManager

    @Inject
    lateinit var shareManager: ShareManager

    @Inject
    lateinit var themeManager: ThemeManager

    private val navController by lazy { findNavController() }

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return ComposeView(requireContext()).apply {
            setContent {
                ChatScreen(
                    onClickHistoryIcon = {
                        navController.navigate(R.id.action_chatFragment_to_historyFragment)
                    },
                    onClickQuickMessageIcon = {
                        navController.navigate(R.id.action_chatFragment_to_messagesFragment)
                    }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        initObservers()
    }

    private fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is ChatState.OpenChat -> {
                    //binding.root.hideKeyboard()
                    whatsAppManager.openChat(state.number, state.message, state.app)
                    analyticsManager.logEvent(AnalyticsEvent.OpenWhatsApp(state.app.name.lowercase()))
                }
                is ChatState.ShareLink -> {
                    //binding.root.hideKeyboard()
                    whatsAppManager.shareLink(state.number, state.message)
                    analyticsManager.logEvent(AnalyticsEvent.ShareChatLink)
                }
                ChatState.InvalidNumber -> toast(R.string.error_invalid_number)
                ChatState.InvalidData -> toast(R.string.error_no_data)
            }
        })

        navController.currentBackStackEntry?.savedStateHandle?.let { handle ->
            handle.getLiveData<String>(EXTRA_MESSAGE).observe(
                viewLifecycleOwner
            ) { message ->
                handle.remove<String>(EXTRA_MESSAGE)
                viewModel.message.value = message
            }
            handle.getLiveData<WhatsAppNumber>(EXTRA_NUMBER).observe(
                viewLifecycleOwner
            ) { number ->
                handle.remove<WhatsAppNumber>(EXTRA_NUMBER)
                viewModel.setFullNumber(number.number, number.code.toString())
            }
        }

        mainViewModel.copiedNumberLiveData.observe(viewLifecycleOwner, { wan ->
            wan?.let { showCopiedNumberInfoDialog(wan) }
        })
    }

    private fun showCopiedNumberInfoDialog(
        wan: WhatsAppNumber,
    ) {
        AlertDialog
            .Builder(requireContext())
            .setMessage(getString(R.string.message_copied_number, wan.fullNumber))
            .setPositiveButton(R.string.action_use) { p0, _ ->
                p0.dismiss()
                wan.code?.let { code -> viewModel.countryCode.value = code.toString() }
                viewModel.mobile.value = wan.number
                //viewModel.setFullNumber(number.number,number.code.toString())
                analyticsManager.logEvent(AnalyticsEvent.CopiedNumber.Use)
            }
            .setNegativeButton(R.string.action_ignore) { p0, _ ->
                p0.dismiss()
                analyticsManager.logEvent(AnalyticsEvent.CopiedNumber.Ignore)
            }
            .setOnDismissListener {
                mainViewModel.resetCopiedNumber()
            }
            .show()
            .also {
                analyticsManager.logEvent(AnalyticsEvent.CopiedNumber.Display)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                navController.navigate(R.id.action_chatFragment_to_aboutFragment)
                analyticsManager.logEvent(AnalyticsEvent.About.Open)
            }
            R.id.action_feedback -> {
                navController.navigate(R.id.action_chatFragment_to_feedbackFragment)
                analyticsManager.logEvent(AnalyticsEvent.Feedback.Open)
            }
            R.id.action_rate -> {
                ratingsManager.rateApp()
                analyticsManager.logEvent(AnalyticsEvent.RateApp)
            }
            R.id.action_share -> {
                shareManager.shareApp()
                analyticsManager.logEvent(AnalyticsEvent.ShareApp)
            }
            R.id.action_theme_dark -> {
                themeManager.setDarkTheme()
                analyticsManager.logEvent(AnalyticsEvent.ChangeTheme)
            }
            R.id.action_theme_light -> {
                themeManager.setLightTheme()
                analyticsManager.logEvent(AnalyticsEvent.ChangeTheme)
            }
            R.id.action_theme_auto -> {
                themeManager.setDefaultTheme()
                analyticsManager.logEvent(AnalyticsEvent.ChangeTheme)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}