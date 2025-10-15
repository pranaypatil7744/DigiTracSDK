package com.example.digitracksdk.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.example.digitracksdk.Constant
import com.innov.digitrac.R
import com.example.digitracksdk.data.source.remote.RetrofitService
import com.example.digitracksdk.domain.model.login_model.FirebaseTokenUpdateRequestModel
import com.example.digitracksdk.domain.model.login_model.FirebaseTokenUpdateResponseModel
import com.example.digitracksdk.presentation.walkthrough.WalkThroughActivity
import com.example.digitracksdk.utils.AppUtils
import com.example.digitracksdk.utils.PreferenceUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.URL

class MyFirebaseMessageService:FirebaseMessagingService() {
    lateinit var preferenceUtils: PreferenceUtils
    override fun onNewToken(t: String) {
        super.onNewToken(t)
        preferenceUtils = PreferenceUtils(applicationContext)
        preferenceUtils.setValue(Constant.PreferenceKeys.FIREBASE_TOKEN, t)
        callUpdateTokenApi()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var title: String? = ""
        var body: String? = ""
        var imageUrl: String? = ""
        val smallIcon: Int = R.drawable.ic_notification
        try {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (remoteMessage.notification?.title != null) {
                title = remoteMessage.notification?.title
            }
            if (remoteMessage.notification?.body != null) {
                body = remoteMessage.notification?.body
            }
            if (remoteMessage.data.containsKey("image_url")) {
                imageUrl = remoteMessage.data["image_url"]
            }
            val redirectionIntent = Intent(applicationContext, WalkThroughActivity::class.java)
            val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(
                    applicationContext,
                    0, redirectionIntent, PendingIntent.FLAG_MUTABLE
                )
            }else {
                PendingIntent.getActivity(
                    applicationContext,
                    0, redirectionIntent, PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
            val oneTimeID = SystemClock.uptimeMillis().toInt()
            val notification: Notification = if (imageUrl?.isNotEmpty() == true) {
                //Notification Image is Received
                NotificationCompat.Builder(this, Constant.CHANNEL_ID)
                    .setSmallIcon(smallIcon)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(
                        NotificationCompat.BigPictureStyle().bigPicture(
                            getBitmapFromUrl(imageUrl)
                        )
                    )
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .build()
            } else {
                //Notification Image is Not Received
                NotificationCompat.Builder(this, Constant.CHANNEL_ID)
                    .setSmallIcon(smallIcon)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .build()
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    Constant.CHANNEL_ID,
                    getString(R.string.default_channel),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(oneTimeID, notification)
        } catch (e: Exception) {
            AppUtils.INSTANCE?.logMe("",e.localizedMessage)
        }
    }

    private fun getBitmapFromUrl(imageUrl: String?): Bitmap? {
        /*
            Used For Handling Images in  Push Notification
         */
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    private fun callUpdateTokenApi(){
        val firebaseToken = preferenceUtils.getValue(Constant.PreferenceKeys.FIREBASE_TOKEN)
        val innovId = preferenceUtils.getValue(Constant.PreferenceKeys.INNOV_ID)
        if ((!firebaseToken.isNullOrEmpty()) && (!innovId.isNullOrEmpty())){
            val service = RetrofitService.create()
            service.callFirebaseTokenUpdateApi(
                FirebaseTokenUpdateRequestModel(
                FirebaseToken = firebaseToken,InnovID = innovId
            )
            )?.enqueue(object :Callback<FirebaseTokenUpdateResponseModel>{
                override fun onResponse(
                    call: Call<FirebaseTokenUpdateResponseModel>,
                    response: Response<FirebaseTokenUpdateResponseModel>
                ) {
                    AppUtils.INSTANCE?.logMe("tokenUpdate", response.message())
                }

                override fun onFailure(call: Call<FirebaseTokenUpdateResponseModel>, t: Throwable) {
                    AppUtils.INSTANCE?.logMe("tokenUpdate", t.localizedMessage)
                }
            })
        }
    }
}