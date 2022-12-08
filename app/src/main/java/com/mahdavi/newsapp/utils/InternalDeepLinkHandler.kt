package com.mahdavi.newsapp.utils

object InternalDeepLinkHandler {
    const val DOMAIN = "myapp://"

    const val AUTH = "${DOMAIN}auth"
    const val DASHBOARD = "${DOMAIN}dashboard"


    fun makeCustomDeepLink(id: String): String {
        return "${DOMAIN}customDeepLink?id=${id}"
    }
}