package com.contestPM.competition.utils

import android.net.Uri

class UrlBuilder {
    public fun builder(url:String): Uri {
        var base_url = Uri.parse(url)
        var builder:Uri.Builder = Uri.Builder()
        builder.scheme(base_url.scheme)
        builder.authority(base_url.host)
        builder.path(base_url.path)
        builder.clearQuery()
        return builder.build()
    }
}