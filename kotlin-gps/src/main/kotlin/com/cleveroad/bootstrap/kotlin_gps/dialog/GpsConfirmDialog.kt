package com.cleveroad.bootstrap.kotlin_gps.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.cleveroad.bootstrap.kotlin_gps.R


class GpsConfirmDialog : DialogFragment() {

    companion object {
        private val MESSAGE_EXTRA = "MESSAGE_EXTRA${GpsConfirmDialog::class.java.simpleName}"
        private val TITLE_EXTRA = "TITLE_EXTRA${GpsConfirmDialog::class.java.simpleName}"
        private val POSITIVE_BUTTON_EXTRA = "POSITIVE_BUTTON_EXTRA${GpsConfirmDialog::class.java.simpleName}"
        private val NEGATIVE_BUTTON_EXTRA = "NEGATIVE_BUTTON_EXTRA${GpsConfirmDialog::class.java.simpleName}"
        fun newInstance(message: String?,
                        title: String?,
                        positiveButton: String?,
                        negativeButton: String?,
                        isCancelable: Boolean) = GpsConfirmDialog().apply {
            arguments = Bundle().apply {
                putString(MESSAGE_EXTRA, message)
                putString(TITLE_EXTRA, title)
                putString(POSITIVE_BUTTON_EXTRA, positiveButton)
                putString(NEGATIVE_BUTTON_EXTRA, negativeButton)
            }
            this.isCancelable = isCancelable
        }
    }

    private var message: String? = null
    private var title: String? = null
    private var positiveButton: String? = null
    private var negativeButton: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.run {
            message = getString(MESSAGE_EXTRA)
            title = getString(TITLE_EXTRA)
            positiveButton = getString(POSITIVE_BUTTON_EXTRA)
            negativeButton = getString(NEGATIVE_BUTTON_EXTRA)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog =
            AlertDialog.Builder(requireContext())
                    .setMessage(message ?: getString(R.string.enable_gps))
                    .setTitle(title ?: getString(R.string.gps))
                    .setPositiveButton(positiveButton ?: getString(R.string.allow)) { _, _ ->
                        dismiss()
                        activity?.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                    .apply { negativeButton?.let { setNegativeButton(it, null) } }
                    .create()
}