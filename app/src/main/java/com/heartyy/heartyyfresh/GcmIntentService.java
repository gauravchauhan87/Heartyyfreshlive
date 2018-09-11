/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.heartyy.heartyyfresh;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.heartyy.heartyyfresh.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCM Demo";
    JSONObject json = null;

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);


        Log.i(TAG, "gcm.toString(): " + intent.getExtras().getString("message"));

        if (!extras.isEmpty()) { // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotification("Deleted messages on server: "
                        + extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                Log.i(TAG, "messageType: " + messageType);

                // Intent i = getIntent();

                Log.i(TAG, "DATATYPE: " + intent.getStringExtra("data"));

                try {
                    json = new JSONObject(intent.getStringExtra("data"));
                    Log.d("json", json.getString("category"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Post notification of received message.
                String noti = intent.getExtras().getString("message");
                if (noti != null) {
                    sendNotification(noti);
                    Log.i(TAG, "Received: " + extras.toString());
                }
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {

        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, HomeActivity.class), 0);

        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        String category = null, orderId = null, displayOrderId = null,webLink = null;
        Intent notificationIntent;

        try {
            category = json.getString("category");
            if (category.equalsIgnoreCase(Constants.TRACK_ORDER)) {
                orderId = json.getString("order_id");
                displayOrderId = json.getString("display_order_id");
            }else if(category.equalsIgnoreCase(Constants.WEB)){
                webLink = json.getString("web_link");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (category.equalsIgnoreCase(Constants.TRACK_ORDER)) {
            notificationIntent = new Intent(this, TrackOrderActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            notificationIntent.putExtra("displayorderId", displayOrderId);
            notificationIntent.putExtra("orderId", orderId);
        } else if (category.equalsIgnoreCase(Constants.PAST_ORDER_SCREEN)) {
            notificationIntent = new Intent(this, OrdersActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            notificationIntent.putExtra("past", "yes");
        } else if (category.equalsIgnoreCase(Constants.STORE_LISTING)) {
            notificationIntent = new Intent(this, HomeActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if (category.equalsIgnoreCase(Constants.WEB)) {
            notificationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webLink));
        } else {
            notificationIntent = new Intent(this, HomeActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        /*note.setLatestEventInfo(this, "Rally", msg, pendingIntent);
        // note.number = count++;
		note.defaults |= Notification.DEFAULT_SOUND;
		note.defaults |= Notification.DEFAULT_VIBRATE;
		note.defaults |= Notification.DEFAULT_LIGHTS;
		note.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, note);*/
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.app_icon);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.food)
                        .setLargeIcon(icon)
                        .setContentTitle("Heartyy Fresh")
                        .setContentText(msg);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(HomeActivity.class);

        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setSound(alarmSound);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
