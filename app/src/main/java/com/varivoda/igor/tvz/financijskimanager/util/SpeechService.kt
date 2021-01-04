package com.varivoda.igor.tvz.financijskimanager.util

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences

class SpeechService {

    companion object {
        fun promptSpeechInput(
            key: Int,
            context: Context,
            fragment: Fragment
        ) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "hr-HR"
            )
            intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 4000)
            intent.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                context.getString(R.string.speech_prompt)
            )
            try {
                fragment.startActivityForResult(intent, key)
            } catch (a: ActivityNotFoundException) {

                alertDialogForGoogleVoiceSearchDownload(context)
            }
        }

        private fun alertDialogForGoogleVoiceSearchDownload(context: Context) {

            val builder = AlertDialog.Builder(context)
            builder.setMessage(context.getString(R.string.google_search_alert_dialog_message))

            builder.setPositiveButton(context.getString(R.string.download)) { _, _ ->
                val linkIntent = Intent(Intent.ACTION_VIEW)
                linkIntent.data =
                    Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.googlequicksearchbox")
                context.startActivity(linkIntent)
            }

            builder.setNegativeButton(context.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }

            builder.show()
        }
    }
}
