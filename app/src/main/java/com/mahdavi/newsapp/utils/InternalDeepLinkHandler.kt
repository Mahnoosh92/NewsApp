package com.mahdavi.newsapp.utils

object InternalDeepLinkHandler {
    const val DOMAIN = "myapp://"

    const val SPLASH = "${DOMAIN}splash"
    const val LOGIN = "${DOMAIN}login"
    const val TABS = "${DOMAIN}tabs"
    const val ONBOARDING = "${DOMAIN}on_boarding"


    fun makeCustomDeepLink(id: String): String {
        return "${DOMAIN}customDeepLink?id=${id}"
    }
}