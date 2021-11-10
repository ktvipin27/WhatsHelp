package com.whatshelp.ui.chat

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.ktvipin27.showcasebubble.BubbleShowCase
import com.github.ktvipin27.showcasebubble.BubbleShowCaseBuilder
import com.github.ktvipin27.showcasebubble.BubbleShowCaseListener
import com.github.ktvipin27.showcasebubble.BubbleShowCaseSequence
import com.whatshelp.R
import com.whatshelp.data.model.WhatsApp
import com.whatshelp.data.model.WhatsAppBusiness
import com.whatshelp.data.model.WhatsAppNumber
import com.whatshelp.databinding.FragmentChatBinding
import com.whatshelp.manager.analytics.AnalyticsEvent
import com.whatshelp.manager.rating.RatingsManager
import com.whatshelp.manager.share.ShareManager
import com.whatshelp.manager.theme.ThemeManager
import com.whatshelp.manager.whatsapp.WhatsAppManager
import com.whatshelp.ui.MainViewModel
import com.whatshelp.ui.base.DBFragment
import com.whatshelp.util.Constants.EXTRA_MESSAGE
import com.whatshelp.util.Constants.EXTRA_NUMBER
import com.whatshelp.util.Constants.Preferences.PREF_SHOWCASE_CHAT_APP
import com.whatshelp.util.Constants.Preferences.PREF_SHOWCASE_CHAT_MESSAGE
import com.whatshelp.util.Constants.Preferences.PREF_SHOWCASE_CHAT_NUMBER
import com.whatshelp.util.NumberUtil
import com.whatshelp.util.hideKeyboard
import com.whatshelp.util.themeColor
import com.whatshelp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ChatFragment : DBFragment<FragmentChatBinding, ChatViewModel>() {

    override val viewModel: ChatViewModel by viewModels()

    override fun getLayoutResource(): Int = R.layout.fragment_chat

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding.tilNumber.setEndIconOnClickListener {
            analyticsManager.logEvent(AnalyticsEvent.OpenLogsAndHistory)
            navController.navigate(R.id.action_chatFragment_to_historyFragment)
        }

        binding.tilMessage.setEndIconOnClickListener {
            navController.navigate(R.id.action_chatFragment_to_messagesFragment)
            analyticsManager.logEvent(AnalyticsEvent.QuickMessage.Open)
        }

        binding.ccp.run {
            registerCarrierNumberEditText(binding.etNumber)
            setOnCountryChangeListener {
                analyticsManager.logEvent(AnalyticsEvent.ChangeCountry(selectedCountryCode,
                    selectedCountryName))
            }
        }

        binding.etNumber.setOnPasteListener { text -> setTextFromClipboard(text) }

        binding.btnSend.setOnClickListener { onClickAction(ChatAction.OPEN_CHAT) }
        binding.btnShareLink.setOnClickListener { onClickAction(ChatAction.SHARE_LINK) }

        binding.btg.addOnButtonCheckedListener { group, checkedId, isChecked ->
            analyticsManager.logEvent(AnalyticsEvent.ChangeChatApp)
            if (checkedId == R.id.btn_whatsapp)
                viewModel.setSelectedApp(WhatsApp)
            else
                viewModel.setSelectedApp(WhatsAppBusiness)
        }

        initObservers()

        initShowCase()
    }

    inner class ShowCaseListener(val dismissListener: () -> Unit) : BubbleShowCaseListener {
        override fun onBackgroundDimClick(bubbleShowCase: BubbleShowCase) {
            bubbleShowCase.dismiss()
            dismissListener()
        }

        override fun onBubbleClick(bubbleShowCase: BubbleShowCase) {
            bubbleShowCase.dismiss()
            dismissListener()
        }

        override fun onCloseActionImageClick(bubbleShowCase: BubbleShowCase) {
            bubbleShowCase.dismiss()
            dismissListener()
        }

        override fun onTargetClick(bubbleShowCase: BubbleShowCase) {
            bubbleShowCase.dismiss()
            dismissListener()
        }

    }

    private fun initShowCase() {

        val showCaseForNumber = BubbleShowCaseBuilder(requireActivity())
            .title(getString(R.string.showcase_title_chat_number))
            .description(getString(R.string.showcase_description_chat_number))
            .arrowPosition(BubbleShowCase.ArrowPosition.TOP)
            .backgroundColor(requireContext().themeColor(R.attr.colorPrimary))
            .textColor(Color.WHITE)
            .showOnce(PREF_SHOWCASE_CHAT_NUMBER)
            .listener(ShowCaseListener { })
            .targetView(binding.tilNumber)

        val showCaseForMessage = BubbleShowCaseBuilder(requireActivity())
            .title(getString(R.string.showcase_title_chat_message))
            .description(getString(R.string.showcase_description_chat_message))
            .arrowPosition(BubbleShowCase.ArrowPosition.TOP)
            .backgroundColor(requireContext().themeColor(R.attr.colorPrimary))
            .textColor(Color.WHITE)
            .showOnce(PREF_SHOWCASE_CHAT_MESSAGE)
            .listener(ShowCaseListener { })
            .targetView(binding.tilMessage)

        val showCaseForAppToggle = BubbleShowCaseBuilder(requireActivity())
            .title(getString(R.string.showcase_title_chat_app_change))
            .description(getString(R.string.showcase_description_chat_app_change))
            .arrowPosition(BubbleShowCase.ArrowPosition.TOP)
            .backgroundColor(requireContext().themeColor(R.attr.colorPrimary))
            .textColor(Color.WHITE)
            .showOnce(PREF_SHOWCASE_CHAT_APP)
            .listener(ShowCaseListener { })
            .targetView(binding.btg)

        BubbleShowCaseSequence()
            .addShowCase(showCaseForNumber)
            .addShowCase(showCaseForMessage)
            .addShowCase(showCaseForAppToggle)
            .show()
    }

    private fun onClickAction(action: ChatAction) {
        with(binding.ccp) {
            viewModel.onClickAction(
                action, isValidFullNumber, selectedCountryCode, fullNumber, formattedFullNumber
            )
        }
    }

    private fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is ChatState.OpenChat -> {
                    binding.root.hideKeyboard()
                    whatsAppManager.openChat(state.number, state.message, state.app)
                    analyticsManager.logEvent(AnalyticsEvent.OpenWhatsApp(state.app.name.lowercase()))
                }
                is ChatState.ShareLink -> {
                    binding.root.hideKeyboard()
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
                binding.etMessage.setText(message)
            }
            handle.getLiveData<String>(EXTRA_NUMBER).observe(
                viewLifecycleOwner
            ) { number ->
                handle.remove<String>(EXTRA_NUMBER)
                if (number.startsWith("+")) binding.ccp.fullNumber = number
                else binding.etNumber.setText(number)
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
                wan.code?.let { code -> binding.ccp.setCountryForPhoneCode(code) }
                binding.etNumber.setText(wan.number)
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

    private fun setTextFromClipboard(text: String) {
        lifecycleScope.launch {
            when (val numberValidity = numberUtil.isValidFullNumber(text)) {
                is NumberUtil.NumberValidity.ValidNumber -> {
                    binding.ccp.setCountryForPhoneCode(numberValidity.code)
                    binding.etNumber.setText(numberValidity.number)
                }
                is NumberUtil.NumberValidity.InvalidCountryCode -> binding.etNumber.setText(
                    numberValidity.number)
                NumberUtil.NumberValidity.InvalidNumber -> {
                }
            }
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

    override fun onDestroyView() {
        binding.ccp.deregisterCarrierNumberEditText()
        super.onDestroyView()
    }
}