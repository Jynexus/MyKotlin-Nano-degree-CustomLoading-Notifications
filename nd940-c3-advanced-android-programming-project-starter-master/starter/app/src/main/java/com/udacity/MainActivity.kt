package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private var notificationId : Int = 0

    private lateinit var notificationManager: NotificationManager

    private lateinit var action: NotificationCompat.Action

    var selectedUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            var ConnectionManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var networkInfo = ConnectionManager.getNetworkCapabilities(ConnectionManager.activeNetwork)

            if(!firstRadio.isChecked && !secRadio.isChecked && !thirdRadio.isChecked){
                Toast.makeText(applicationContext, "Please select a file to download", Toast.LENGTH_SHORT).show()
            }
            else if (networkInfo != null && networkInfo.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                custom_button.animateBtn()
                firstRadio.isEnabled = false
                secRadio.isEnabled =false
                thirdRadio.isEnabled = false

                download()
            }
            else {
                Toast.makeText(applicationContext, "No Internet connection to download", Toast.LENGTH_SHORT).show()
            }
        }
        firstRadio.setOnCheckedChangeListener{
                buttonView, isChecked -> if(isChecked) {selectedUrl= buttonView.text.toString()}
        }
        secRadio.setOnCheckedChangeListener{
                buttonView, isChecked -> if(isChecked) {selectedUrl= buttonView.text.toString()}
        }
        thirdRadio.setOnCheckedChangeListener{
                buttonView, isChecked -> if(isChecked) {selectedUrl= buttonView.text.toString()}
        }

        notificationManager =
            ContextCompat.getSystemService(
                application,
                NotificationManager::class.java
            ) as NotificationManager

        createChannel(
            "1",
            "Download Channel"
        )
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            var downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

            var uri = downloadManager.getUriForDownloadedFile(id!!)

            var success = true

            if (uri == null) {
                success = false
            }

            custom_button.resetBtn()
            sendNotification(success, selectedUrl)

            firstRadio.isEnabled = true
            secRadio.isEnabled = true
            thirdRadio.isEnabled = true
        }
    }

    private fun download() {
        try {
            val request =
                DownloadManager.Request(Uri.parse(selectedUrl))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        }
        catch (t:Exception){
            firstRadio.isEnabled = true
            secRadio.isEnabled = true
            thirdRadio.isEnabled = true
            custom_button.resetBtn()
            sendNotification(false,selectedUrl)
        }

    }


    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // TODO: Step 2.4 change importance
                NotificationManager.IMPORTANCE_HIGH
            )// TODO: Step 2.6 disable badges for this channel
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download Repository"

            notificationManager.createNotificationChannel(notificationChannel)

        }
        // TODO: Step 1.6 END create a channel
    }

    fun sendNotification(success :Boolean, selectedUrl :String ){
        // TODO: Step 1.11 create intent
        val contentIntent = Intent(applicationContext, MainActivity::class.java)
        // TODO: Step 1.12 create PendingIntent
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            notificationId,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // TODO: Step 2.2 add CheckStatus action
        val statusIntent = Intent(applicationContext, DetailActivity::class.java)
        statusIntent.putExtra("File",selectedUrl)
        statusIntent.putExtra("Status",success)
        val statusPendingIntent = PendingIntent.getActivity(
            applicationContext,
            notificationId,
            statusIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(applicationContext,"1")

            // TODO: Step 1.8 use the new 'breakfast' notification channel

            // TODO: Step 1.3 set title, text and icon to builder
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(applicationContext
                .getString(R.string.notification_title))
            .setContentText("The Project repository is downloaded")

            // TODO: Step 1.13 set content intent
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            // TODO: Step 2.3 add snooze action
            .addAction(
                R.drawable.ic_assistant_black_24dp,
                "Check download status",
                statusPendingIntent
            )

            // TODO: Step 2.5 set priority
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        // TODO: Step 1.4 call notify
        notificationManager.notify(notificationId, builder.build())
        notificationId++
    }


    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
