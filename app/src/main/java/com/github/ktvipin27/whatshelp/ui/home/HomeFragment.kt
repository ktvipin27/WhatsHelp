package com.github.ktvipin27.whatshelp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.ktvipin27.whatshelp.R
import com.github.ktvipin27.whatshelp.databinding.FragmentHomeBinding
import android.widget.Toast

import android.content.Intent

import android.content.pm.PackageManager

import android.content.pm.PackageInfo
import android.net.Uri


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.btnSend.setOnClickListener { onClickWhatsApp(binding.etNumber.text.toString()) }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClickWhatsApp(mobile:String) {
        val isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp")

        if (isWhatsappInstalled) {
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$mobile&text=Hi")
            val sendIntent = Intent(Intent.ACTION_VIEW, uri);
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        } else {
            Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun whatsappInstalledOrNot(uri: String): Boolean = try {
        requireActivity().packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}