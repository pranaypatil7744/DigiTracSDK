package com.example.digitracksdk.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status


class SMSBroadcastReceiver : BroadcastReceiver() {
    lateinit var smsBroadcastReceiverListener: SmsBroadcastReceiverListener

    override fun onReceive(context: Context?, intent: Intent?) {

        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {

            val bundle = intent.extras
            val smsRetrieverStatus = bundle?.get(SmsRetriever.EXTRA_STATUS) as Status
            when (smsRetrieverStatus.statusCode) {
                CommonStatusCodes.SUCCESS -> {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        bundle.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT, Intent::class.java)
                            .also {
                                smsBroadcastReceiverListener.onSuccess(it)
                            }
                    } else
                        bundle.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT).also {
                            smsBroadcastReceiverListener.onSuccess(it)
                        }
                }
                CommonStatusCodes.TIMEOUT -> {
                    smsBroadcastReceiverListener.onFailure()
                }
            }
        }
    }

    interface SmsBroadcastReceiverListener {
        fun onSuccess(intent: Intent?)
        fun onFailure()
    }

}