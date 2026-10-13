package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity

object SecurityNotificationManager {
    const val CHANNEL_APPROVAL = "channel_wifi_approval"
    const val CHANNEL_ALERTS = "channel_wifi_security_alerts"

    fun initNotificationChannels(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val approvalChannel = NotificationChannel(
                    CHANNEL_APPROVAL,
                    "Device Approval Requests",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Urgent alerts when a new device requests Wi-Fi access"
                    enableVibration(true)
                }

                val alertsChannel = NotificationChannel(
                    CHANNEL_ALERTS,
                    "Security & Router Alerts",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Network security events, unknown device alerts, and router status changes"
                }

                val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                manager?.createNotificationChannel(approvalChannel)
                manager?.createNotificationChannel(alertsChannel)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun showApprovalNotification(
        context: Context,
        requestId: String,
        hostname: String,
        macAddress: String,
        ipAddress: String
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("NAVIGATE_TO", "pending_approvals")
            putExtra("REQUEST_ID", requestId)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            requestId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_APPROVAL)
            .setSmallIcon(android.R.drawable.ic_lock_lock)
            .setContentTitle("New Device Requesting Wi-Fi Access")
            .setContentText("$hostname ($macAddress) held pending approval.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("A new device '$hostname' ($ipAddress, MAC: $macAddress) is attempting to access your Wi-Fi network. Tap to approve or reject.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(requestId.hashCode(), notification)
        } catch (_: SecurityException) {
            // Handled gracefully if notification permission not yet granted by user
        }
    }

    fun cancelNotification(context: Context, requestId: String) {
        NotificationManagerCompat.from(context).cancel(requestId.hashCode())
    }
}
