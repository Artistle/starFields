package com.contestPM.competition.utils

import android.net.Uri

class UrlBuilder {
    public fun builder(base_url:Uri): Uri {
        var builder:Uri.Builder = Uri.Builder()
        builder.scheme(base_url.scheme)
        builder.authority(base_url.host)
        builder.path(base_url.path)
        builder.clearQuery()
        return builder.build()
    }
}