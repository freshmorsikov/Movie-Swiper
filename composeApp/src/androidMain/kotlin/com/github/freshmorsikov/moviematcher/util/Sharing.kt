package com.github.freshmorsikov.moviematcher.util

import android.content.Context
import android.content.Intent
import org.koin.dsl.module

actual val sharingModule = module {
    factory<SharingManager> {
        AndroidSharingManager(context = get())
    }
}

class AndroidSharingManager(
    private val context: Context
) : SharingManager {

    override fun share(title: String, text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(
            Intent.createChooser(shareIntent, "Share via").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }

}