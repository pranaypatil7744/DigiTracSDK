package com.example.digitracksdk.service

import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.digitracksdk.Constant
import com.example.digitracksdk.utils.PreferenceUtils
import me.leolin.shortcutbadger.ShortcutBadger

class NotificationListener:NotificationListenerService() {

    lateinit var context:Context
    lateinit var preferenceUtils: PreferenceUtils

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        preferenceUtils = PreferenceUtils(context)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val pack = sbn?.packageName
        val extras = sbn?.notification?.extras
        val title = extras?.getString("android.title")

        if (pack.equals("com.innov.digitrac", ignoreCase = true)) {
            var countString: String? = preferenceUtils.getValue(Constant.PreferenceKeys.NotificationCount)
            if (countString != null) {
                countString = if (countString.equals("", ignoreCase = true)) "0" else countString
                var unReadNotiCount = countString.toInt()
                unReadNotiCount += 1
                preferenceUtils.setValue(Constant.PreferenceKeys.NotificationCount,""+unReadNotiCount)
                ShortcutBadger.applyCount(context, unReadNotiCount)
            }
        }

        val msgrcv = Intent("Msg")
        msgrcv.putExtra("package", pack)
        msgrcv.putExtra("title", title)
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        if (sbn!!.packageName.equals("com.innov.digitrac", ignoreCase = true)) {
            var countString: String? =
                preferenceUtils.getValue(Constant.PreferenceKeys.NotificationCount)
            if (countString != null) {
                countString = if (countString.equals("", ignoreCase = true)) "0" else countString
                var unReadNotiCount = countString.toInt()
                if (unReadNotiCount > 0) {
                    unReadNotiCount -= 1
                    preferenceUtils.setValue(Constant.PreferenceKeys.NotificationCount,""+unReadNotiCount)
                    ShortcutBadger.applyCount(context, unReadNotiCount)
                }
            }
        }
    }
}