package com.hal9k.notify4scripts;

import android.support.v4.app.NotificationCompat;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.app.IntentService;

public class NotifyService extends IntentService {
	public static final String KEY_TICKER = "str_ticker";
	public static final String KEY_CONTENTTITLE = "str_title";
	public static final String KEY_CONTENTTEXT = "str_content";
	public static final String KEY_ID = "int_id";

	PendingIntent contentIntent;
	NotificationCompat.Builder notification;
	NotificationManager manager;
	Intent notificationIntent;

	public NotifyService() {
		super("NotifyService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
	
	    String str_ticker = null;
		String str_title = null;
		String str_content = null;
		String int_id = null;
		int ID4notify = 0;
		
		if (null != intent) {
			str_ticker = intent.getStringExtra(KEY_TICKER);
			str_title = intent.getStringExtra(KEY_CONTENTTITLE);
			str_content = intent.getStringExtra(KEY_CONTENTTEXT);
			int_id = intent.getStringExtra(KEY_ID);
		}

		if (null == str_ticker) {
			str_ticker = "Notify4Scripts ticker";
		}
		
		if (null == str_title) {
			str_title = "Notify4Scripts Notification Title";
		}
		
		if (null == str_content) {
			str_content = "Notify4Scripts Notification Content";
		}

		if (null != int_id) {
			try {
				ID4notify = Integer.parseInt(int_id);
			}
			catch(NumberFormatException ex) {
				// Handle the condition when str is not a number.
				ID4notify = 0; 
			}
		}
	
		// Creating Notification Builder
		notification = new NotificationCompat.Builder(NotifyService.this);
		// Title for Notification
		notification.setContentTitle(str_title);		
		// Message in the Notification
		notification.setContentText(str_content);
		// Alert shown when Notification is received
		notification.setTicker(str_ticker);
		// Icon to be set on Notification
		notification.setSmallIcon(R.drawable.notify4scripts_icon);

		// This flag will make it so the notification is automatically
		// canceled when the user clicks it in the panel.
		notification.setAutoCancel(true);
		
		// No action will be performed when notification is clicked
		// so no intent is needed and the following lines are commented out
		//
		// resultIntent = new Intent(MainActivity.this, Result.class);
		// stackBuilder.addNextIntent(resultIntent);
		// pIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		
		notificationIntent = new Intent();
		contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

		notification.setContentIntent(contentIntent);

		notification.build().defaults |= Notification.DEFAULT_SOUND;
		
		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(ID4notify, notification.build());

	}
	
}

