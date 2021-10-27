package com.whatshelp.data.model

import com.whatshelp.util.Constants

/**
 * Created by Vipin KT on 27/10/21
 */
open class App(val name: String,val packageName:String)

object WhatsApp: App("WhatsApp", Constants.PACKAGE_WHATSAPP)

object WhatsAppBusiness: App("WhatsApp Business", Constants.PACKAGE_WHATSAPP_BUSINESS)