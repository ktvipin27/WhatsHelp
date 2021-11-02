package com.whatshelp.data.model

/**
 * Created by Vipin KT on 27/10/21
 */
interface App {
    val name: String
    val packageName: String
}

object WhatsApp : App {
    override val name: String = "WhatsApp"
    override val packageName: String = "com.whatsapp"
}

object WhatsAppBusiness : App {
    override val name: String = "WhatsApp Business"
    override val packageName: String = "com.whatsapp.w4b"
}