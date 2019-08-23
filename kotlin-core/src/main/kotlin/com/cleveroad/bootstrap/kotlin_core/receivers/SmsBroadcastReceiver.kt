package com.cleveroad.bootstrap.kotlin_core.receivers


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage

/**
 * Receiver for detecting new sms
 * Do not forget to get permission for sms reading!
 */
abstract class SmsBroadcastReceiver : BroadcastReceiver() {

    companion object {
        private const val PDUS = "pdus"  //protocol description units
        private const val FORMAT = "format"
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION != intent?.action) {
            return
        }
        val bundle = intent.extras ?: return
        @Suppress("UNCHECKED_CAST")
        val pdus = (bundle.getSerializable(PDUS) as? Array<Any>) ?: return
        val format = intent.getStringExtra(FORMAT)

        val msgs = arrayOfNulls<SmsMessage>(pdus.size)
        msgs.forEachIndexed { i, _ ->
            msgs[i] = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                @Suppress("DEPRECATION")
                SmsMessage.createFromPdu(pdus[i] as? ByteArray)
            } else {
                SmsMessage.createFromPdu(pdus[i] as? ByteArray, format)
            }
            msgs[i]?.takeIf { !it.messageBody.isNullOrEmpty() }?.apply { onMessageReceived(messageBody) }
        }
    }

    /**
     * Method to accept messageBody text
     */
    protected abstract fun onMessageReceived(msgBody: String)

    /**
     * Method to accept full message info
     */
    protected open fun onMessageReceived(smsMessage: SmsMessage) = onMessageReceived(smsMessage.messageBody)
}
