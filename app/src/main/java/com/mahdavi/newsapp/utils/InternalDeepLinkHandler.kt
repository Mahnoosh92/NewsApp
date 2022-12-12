package com.mahdavi.newsapp.utils

object InternalDeepLinkHandler {
    const val DOMAIN = "myapp://"

    const val AUTH = "${DOMAIN}auth"
    const val TABS = "${DOMAIN}tabs"


    fun makeCustomDeepLink(id: String): String {
        return "${DOMAIN}customDeepLink?id=${id}"
    }
}