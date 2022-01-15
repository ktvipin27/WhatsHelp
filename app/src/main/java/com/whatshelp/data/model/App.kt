package com.whatshelp.data.model

import com.whatshelp.R

/**
 * Created by Vipin KT on 27/10/21
 */
interface App {
    val name: String
    val packageName: String
    val icon: Int
}

object WhatsApp : App {
    override val name: String = "WhatsApp"
    override val packageName: String = "com.whatsapp"
    override val icon: Int = R.drawable.ic_whatsapp
}

object WhatsAppBusiness : App {
    override val name: String = "WhatsApp Business"
    override val packageName: String = "com.whatsapp.w4b"
    override val icon: Int = R.drawable.ic_whatsapp_business
}