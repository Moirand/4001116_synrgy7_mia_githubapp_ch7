@file:JvmName("Constants")
package com.example.githubapp.worker

@JvmField val NOTIFICATION_TITLE: CharSequence = "WorkRequest Starting"
@JvmField val VERBOSE_NOTIFICATION_CHANNEL_NAME: CharSequence = "Verbose WorkManager Notifications"
const val VERBOSE_NOTIFICATION_CHANNEL_DESCRIPTION = "Shows notifications whenever work starts"
const val CHANNEL_ID = "VERBOSE_NOTIFICATION"
const val OUTPUT_PATH = "blur_filter_outputs"
const val TAG_OUTPUT = "OUTPUT"
const val NOTIFICATION_ID = 1
const val DELAY_TIME_MILLIS: Long = 3000