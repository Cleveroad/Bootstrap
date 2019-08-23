package com.cleveroad.bootstrap.kotlin_gps.dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.cleveroad.bootstrap.kotlin_gps.R


class AllowPermissionDialog : DialogFragment() {

    companion object {
        private const val PACKAGE_CONST = "package:"
        private val MESSAGE_EXTRA = "MESSAGE_EXTRA${AllowPermissionDialog::class.java.simpleName}"
        private val TITLE_EXTRA = "TITLE_EXTRA${AllowPermissionDialog::class.java.simpleName}"
        private val POSITIVE_BUTTON_EXTRA = "POSITIVE_BUTTON_EXTRA${AllowPermissionDialog::class.java.simpleName}"
        private val NEGATIVE_BUTTON_EXTRA = "NEGATIVE_BUTTON_EXTRA${AllowPermissionDialog::class.java.simpleName}"
        private val APPLICATION_ID_EXTRA = "APPLICATION_ID_EXTRA${AllowPermissionDialog::class.java.simpleName}"
        fun newInstance(applicationId: String,
                        title: String? = null,
                        message: String? = null,
                        positiveButton: String? = null,
                        negativeButton: String? = null,
                        isCancelable: Boolean = false
        ) = AllowPermissionDialog().apply {
            arguments = Bundle().apply {
                putString(MESSAGE_EXTRA, message)
                putString(TITLE_EXTRA, title)
                putString(POSITIVE_BUTTON_EXTRA, positiveButton)
                putString(APPLICATION_ID_EXTRA, applicationId)
                putString(NEGATIVE_BUTTON_EXTRA, negativeButton)
            }
            this.isCancelable = isCancelable
        }
    }

    private var title: String? = null
    private var message: String? = null
    private var positiveButton: String? = null
    private var negativeButton: String? = null
    private var applicationId: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.run {
            title = getString(TITLE_EXTRA)
            message = getString(MESSAGE_EXTRA)
            positiveButton = getString(POSITIVE_BUTTON_EXTRA)
            negativeButton = getString(NEGATIVE_BUTTON_EXTRA)
            applicationId = getString(APPLICATION_ID_EXTRA)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog =
            AlertDialog.Builder(requireContext())
                    .setTitle(title)
                    .setMessage(message ?: getString(R.string.permission_denied))
                    .apply { negativeButton?.let { setNegativeButton(it, null) } }
                    .setPositiveButton(positiveButton ?: getString(R.string.allow)) { _, _ ->
                        dismiss()
                        activity?.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("$PACKAGE_CONST$applicationId")))
                    }
                    .create()
}