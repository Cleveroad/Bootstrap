package com.cleveroad.bootstrap.kotlin_core.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment


/**
 * Base fragment for the implementation of the dialog with custom view
 */
abstract class BaseDialogFragment : DialogFragment() {

    /**
     * Set id of layout.
     */
    protected abstract val layoutId: Int

    /**
     * Set [Gravity] of this dialog window.
     */
    protected abstract val gravity: Int

    /**
     * Set width the dialog window.
     */
    protected abstract val dialogWidth: Int

    /**
     * Set height the dialog window.
     */
    protected abstract val dialogHeight: Int

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(layoutId, container, false)

    override fun onCreateDialog(savedInstanceState: Bundle?) =
            super.onCreateDialog(savedInstanceState).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
            }

    override fun onResume() {
        super.onResume()
        dialog?.window?.run {
            attributes.gravity = gravity
            setLayout(dialogWidth, dialogHeight)
        }
    }
}
