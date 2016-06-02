package com.hal9k.notify4scripts;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Locale;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.app.IntentService;

public class NotifyServiceCV extends IntentService {

	PendingIntent contentIntent;
	Notification notification;
	NotificationManager manager;
	Intent notificationIntent;
	DisplayMetrics metrics;
	
    public NotifyServiceCV() {
		super("NotifyServiceCV");
	}

    @Override
	protected void onHandleIntent(Intent intent) {
	
	    String str_ticker = null;
		String str_title = null;
		String str_content = null;
		String int_id = null;
		String hex_tcolor = null;
		String hex_ccolor = null;
		String float_tsize = null;
		String float_csize = null;
		String b_noicon = null;
		String b_notime = null;
		String str_viewuri = null;
		String str_exec = null;
		String b_execdebug = null;
		String b_customview = null;
		String str_staticonuri = null;
		String str_iconuri = null;
		String int_iconid = null;
		short ID4icon = 0;
		int ID4notify = 0;
		
		RemoteViews customContentView = new RemoteViews(getPackageName(), R.layout.custom_notification_layout);
		
		if (null != intent) {
			str_ticker = intent.getStringExtra("str_ticker");
			str_title = intent.getStringExtra("str_title");
			str_content = intent.getStringExtra("str_content");
			int_id = intent.getStringExtra("int_id");
			hex_tcolor = intent.getStringExtra("hex_tcolor");
			hex_ccolor = intent.getStringExtra("hex_ccolor");
			float_tsize = intent.getStringExtra("float_tsize");
			float_csize = intent.getStringExtra("float_csize");
			b_noicon = intent.getStringExtra("b_noicon");
			b_notime = intent.getStringExtra("b_notime");
			str_viewuri = intent.getStringExtra("str_viewuri");
			str_exec = intent.getStringExtra("str_exec");
			b_execdebug = intent.getStringExtra("b_execdebug");
			b_customview = intent.getStringExtra("b_customview");
			str_staticonuri = intent.getStringExtra("str_staticonuri");
			str_iconuri = intent.getStringExtra("str_iconuri");
			int_iconid = intent.getStringExtra("int_iconid");
		}

		if (null == str_ticker) {
			str_ticker = "Notify4Scripts ticker";
			} else if ("".equals(str_ticker)) {
			str_ticker = null;
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
				Log.d("Notify4Scripts - ID4notify","catched NumberFormatException for int_id = "+ int_id);
			}
		}
	
		// Custom notification view that actually
		// override the use of setContentTitle and setContentText
		// Initializing the style attributes for the notification view
		discoverStyle();
		float NotifyTimeSize = mNotifyTextSize;
		
        Log.i("Notify4Scripts - discoverStyle()",
        	"Default Title color = " + Integer.toString(mNotifyTitleColor) +"; "+
        	"Default Title size = " + Float.toString(mNotifyTitleSize) +"; "+
        	"Default Content color = " + Integer.toString(mNotifyTextColor) +"; "+
        	"Default Content size = " + Float.toString(mNotifyTextSize) +"; "+
        	"Current scaledDensity = " + Float.toString(mScaledDensity)
        );
		
        if (null != hex_tcolor && hex_tcolor.length() == 6) {
			try {
				// Had much trouble trying to figure out how to properly parse the hex string as
				// Integer.parseInt(hex_color,16) failed
				// Finally the solution came from user ashish-sahu http://stackoverflow.com/users/1780737/ashish-sahu
				// that commented this page
				// http://stackoverflow.com/questions/11377944/parsing-a-hexadecimal-string-to-an-integer-throws-a-numberformatexception
				mNotifyTitleColor = Color.parseColor("#"+hex_tcolor);
				b_customview = "1";
			}
			catch(IllegalArgumentException ex) {
				// Handle the condition when hex_color not a valid hex number
				Log.d("Notify4Scripts - TitleColor","catched IllegalArgumentException for hex_tcolor = "+ hex_tcolor);
			}
		}

        if (null != hex_ccolor && hex_ccolor.length() == 6) {
			try {
				/* Had much trouble trying to figure out how to properly parse the hex string as
				 * Integer.parseInt(hex_color,16) failed
				 * Finally the solution came from user ashish-sahu http://stackoverflow.com/users/1780737/ashish-sahu
				 * that commented this page
				 * http://stackoverflow.com/questions/11377944/parsing-a-hexadecimal-string-to-an-integer-throws-a-numberformatexception
				 */
				mNotifyTextColor = Color.parseColor("#"+hex_ccolor);
				b_customview = "1";
			}
			catch(IllegalArgumentException ex) {
				// Handle the condition when hex_color not a valid hex number
				Log.d("Notify4Scripts - ContentColor","catched IllegalArgumentException for hex_ccolor = "+ hex_ccolor);
			}
		}

		if (null != float_tsize) {
			try {
				mNotifyTitleSize = Float.parseFloat(float_tsize);
				b_customview = "1";
			}
			catch(NumberFormatException ex) {
				// Handle the condition when str is not a number.
				Log.d("Notify4Scripts - TitleSize","catched NumberFormatException for float_tsize = "+ float_tsize);
			}
		}
	
		if (null != float_csize) {
			try {
				mNotifyTextSize = Float.parseFloat(float_csize);
				b_customview = "1";
			}
			catch(NumberFormatException ex) {
				// Handle the condition when str is not a number.
				Log.d("Notify4Scripts - ContentSize","catched NumberFormatException for float_csize = "+ float_csize);
			}
		}
	
		if ("".equals(str_title)) {
			customContentView.setViewVisibility(R.id.notification_title, View.GONE); b_customview = "1";}
		if ("1".equals(b_noicon)) {
			customContentView.setViewVisibility(R.id.notification_image, View.GONE); b_customview = "1";}
		if ("1".equals(b_notime)) {
			customContentView.setViewVisibility(R.id.notification_time, View.GONE); b_customview = "1";}
	
		/* The following code join both str_title and str_content in one spannable string
		 * where the str_title portion is set to bold style
		 * A newline is added between title and content
		 * The code is been found first on
		 * http://www.programcreek.com/java-api-examples/index.php?api=android.text.style.AbsoluteSizeSpan
		 * The code is been commented out as from the initial idea of joining title and content
		 * I revert back to keep both separated in their own dedicated textView. 
		 * 
		SpannableStringBuilder notification_text;
		
		if (str_title.length() != 0) { 
			notification_text = new SpannableStringBuilder(str_title);
			notification_text.setSpan(new StyleSpan(Typeface.BOLD),0,str_title.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			notification_text.setSpan(new AbsoluteSizeSpan((int) (mNotifyTitleSize * mScaledDensity)),0,str_title.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			notification_text.setSpan(new ForegroundColorSpan(mNotifyTitleColor),0,str_title.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			notification_text.append(System.getProperty("line.separator"));
			notification_text.append(str_content);
		} else {
			notification_text = new SpannableStringBuilder(str_content);
		}
		*/
        
		// Building the time text 
		Time currentTime = new Time(Time.getCurrentTimezone());
		currentTime.setToNow();
		
		// For system icons resources look at here http://androiddrawableexplorer.appspot.com/
		// Java Usage example: myMenuItem.setIcon(android.R.drawable.ic_menu_save);
		// Resource Usage example: android:icon="@android:drawable/ic_menu_save"
		
		//String str_staticonuri = null;
		//String str_iconuri = null;
		//
		/*String DBcontent_text = "";
		if (null != str_staticonuri) {
			try {
				InputStream inputStream = getContentResolver().openInputStream(
						Uri.parse("android.resource://" + getPackageName() +
						"/drawable/" + "stat_icons[Short.parseShort(int_iconid)]")));
			        if (str_staticonuri.contains("http")){
			        	Drawable yourDrawable = Drawable.createFromStream(((java.io.InputStream) new java.net.URL(str_staticonuri).getContent()), "name");
			        } else {
			        	Drawable yourDrawable = Drawable.createFromPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/pdfthumbnail5/helloworld.jpg").toString());
			        }
			}
			catch(NumberFormatException ex) {
				// Handle the condition when str is not a number.
				Log.d("Notify4Scripts - int_iconid","catched NumberFormatException for int_id = "+ int_id);
			}
		}*/
		
		// Choosing the notification icon
		String[] stat_icons = getResources().getStringArray(R.array.stat_ic_array);

		if (null != int_iconid) {
			try {
				ID4icon = Short.parseShort(int_iconid);
				if (ID4icon < 0 || ID4icon >= stat_icons.length) {
					Log.d("Notify4Scripts - int_iconid","invalid icon ID for int_iconid = "+ int_iconid + " valid ID must be in the range 0-" + (stat_icons.length - 1));
					ID4icon = 0;
				}
			}
			catch(NumberFormatException ex) {
			// Handle the condition when str is not a number.
			Log.d("Notify4Scripts - int_iconid","catched NumberFormatException for int_iconid = "+ int_iconid);
			}
		}
	
		int drawableId = getResources().getIdentifier(stat_icons[ID4icon].contains(":") ? stat_icons[ID4icon] : this.getPackageName()+":drawable/"+stat_icons[ID4icon], null, null);

		if (drawableId == 0 ) {
			drawableId = getResources().getIdentifier(this.getPackageName()+":drawable/"+stat_icons[0], null, null);
			Log.d("Notify4Scripts - int_iconid","no drawable resource found for int_iconid = "+ int_iconid + "; resource = "+stat_icons[ID4icon]);
		}


		getPackageResources("jackpal.androidterm");
		
		try {
	        PackageManager manager = getPackageManager();
	        Resources mApk1Resources = manager.getResourcesForApplication("jackpal.androidterm");

	        int mDrawableResID = mApk1Resources.getIdentifier("ic_launcher", "drawable","jackpal.androidterm");

	        Drawable myDrawable = mApk1Resources.getDrawable( mDrawableResID );

	        if( myDrawable != null )
				Log.d("Notify4Scripts - int_iconid","no drawable resource found in jackpal.androidterm package");

	    }
	    catch (NameNotFoundException e) {
			Log.d("Notify4Scripts - int_iconid","NameNotFoundException in jackpal.androidterm package");
	    }

		
		customContentView.setImageViewResource(R.id.notification_image, drawableId);
                 
		customContentView.setTextViewText(R.id.notification_title,str_title.replace("\\n", "\n"));
		customContentView.setTextColor(R.id.notification_title, mNotifyTitleColor);
		customContentView.setFloat(R.id.notification_title, "setTextSize", mNotifyTitleSize);
		
		customContentView.setTextViewText(R.id.notification_text,str_content.replace("\\n", "\n"));
		customContentView.setTextColor(R.id.notification_text, mNotifyTextColor);
		customContentView.setFloat(R.id.notification_text, "setTextSize", mNotifyTextSize);

		// Hints for currentTime.format string found here http://linux.die.net/man/3/strftime
		customContentView.setTextViewText(R.id.notification_time,DateFormat.is24HourFormat(this) ?
				currentTime.format("%H:%M") : 
				currentTime.format("%I:%M")+" "+currentTime.format("%p").toUpperCase(Locale.US));
		customContentView.setTextColor(R.id.notification_time, mNotifyTextColor);
		customContentView.setFloat(R.id.notification_time, "setTextSize", NotifyTimeSize);

		notificationIntent = new Intent();

		if (null != str_viewuri) {
		    notificationIntent = new Intent(Intent.ACTION_VIEW);
			String filext = str_viewuri.substring(str_viewuri.lastIndexOf(".") + 1, str_viewuri.length());
			notificationIntent.setDataAndType(Uri.parse(str_viewuri),MimeTypeMap.getSingleton().getMimeTypeFromExtension(filext));

			Log.d("Notify4Scripts - MimeType","Recognized MimeType for file "+str_viewuri+
				" is: "+MimeTypeMap.getSingleton().getMimeTypeFromExtension(filext)+" FileExt is: "+filext);
			} 
		else if (null != str_exec) {
		    notificationIntent = new Intent(NotifyServiceCV.this, NotificationAction.class)
		    .putExtra("b_execdebug", b_execdebug)
		    .putExtra("str_exec", str_exec);

		    Log.i("Notify4Scripts - CustomAction","The action for notification id="+int_id+" is been set to this: "+str_exec);
		}
		
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
    
       
		/* Thanks to answer of user keide http://stackoverflow.com/users/930653/keide
		 * found here http://stackoverflow.com/questions/6422319/start-service-from-notification
		 *
		 * Other problems here:
		 * 1) the service didn't receive the extra (on real device, but on emulator worked) because
		 *    the PendingIntent.FLAG_* was set to 0
		 *    Despite many searches I haven't been able to find the reason of the extra not passed to 
		 *    the service, then finally I tried to explicitly set the .FLAG_UPDATE_CURRENT and that fixed!
		 * 2) If having more than one notification, there will be one only PendingIntent for all unless
		 *    the PendingIntent have at least one different among their action, data, type, class, and categories
		 *    Solution found here: http://stackoverflow.com/questions/3009059/android-pending-intent-notification-problem
		 *    Thanks to user CommonsWare http://stackoverflow.com/users/115145/commonsware
		 */
        notificationIntent.setType(int_id); 
		
		contentIntent = PendingIntent.getService(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		// Creating Notification Builder
		notification = new NotificationCompat.Builder(NotifyServiceCV.this)
		// Title for Notification
		.setContentTitle(str_title)
		// Message in the Notification
		.setContentText(str_content)
		// Alert shown when Notification is received
		.setTicker(str_ticker)
		// Icon to be set on Notification
		.setSmallIcon(drawableId)

		// This flag will make it so the notification is automatically
		// canceled when the user clicks it in the panel.
		//.setAutoCancel(true)
		
		/* No action will be performed when notification is clicked
		 * so no intent is needed and the following lines are commented out
		 *
		 * resultIntent = new Intent(MainActivity.this, Result.class);
		 * stackBuilder.addNextIntent(resultIntent);
		 * pIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		 */
		.setContentIntent(contentIntent)

		.build();
		
		//Check if CustomView has to be used in place of default view
		if (b_customview != null && b_customview.equals("1")) {
				
			// Despite what can be read in many articles .setContent() doesn't
			// work but need to set .contentView with a = operator.
			notification.contentView = customContentView;
		}
		
		// Default notification sound is only played if a ticker is set
		if (null != str_ticker) {notification.defaults |= Notification.DEFAULT_SOUND;}
		//notification.sound = Uri.parse("file:///mnt/sdcard/MediaFiles/08_Pure_Bell.ogg");
		
		//notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
		
		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(ID4notify, notification);

	}
	
	/* The following code is been found first on http://stackoverflow.com/questions/4867338/custom-notification-layouts-and-text-colors
	 * posted by user Gask http://stackoverflow.com/users/306831/gaks 
	 * But actually is been almost straight copy/pasted from here:
	 * https://github.com/derekbrameyer/Noteify/blob/master/src/com/doomonafireball/noteify/util/PreFroyoNotificationStyleDiscover.java
	 */
	private Integer mNotifyTextColor = null;
    private float mNotifyTextSize = 11;
    private Integer mNotifyTitleColor = null;
    private float mNotifyTitleSize = 12;
    private float mScaledDensity;
    private final String TEXT_SEARCH_TEXT = "SearchForText";
    private final String TEXT_SEARCH_TITLE = "SearchForTitle";
 
    public int getTextColor() {
        return mNotifyTextColor.intValue();
    }

    public float getTextSize() {
        return mNotifyTextSize;
    }

    public int getTitleColor() {
        return mNotifyTitleColor.intValue();
    }

    public float getTitleSize() {
        return mNotifyTitleSize;
    }

    private void discoverStyle() {
        if (null != mNotifyTextColor) {
            return;
        }

        try {
		// Creating Notification
		notification = new NotificationCompat.Builder(NotifyServiceCV.this)
			.setContentTitle(TEXT_SEARCH_TITLE)
			.setContentText(TEXT_SEARCH_TEXT)
			.setContentIntent(null)
			.build();

			LinearLayout group = new LinearLayout(this);
	        ViewGroup event = (ViewGroup) notification.contentView.apply(this, group);
	        recurseGroup(event);
	        group.removeAllViews();
	    } catch (Exception e) {
        mNotifyTextColor = android.R.color.white;
        mNotifyTitleColor = android.R.color.white;
	    }
    }
	
    private boolean recurseGroup(ViewGroup group) {
        final int count = group.getChildCount();

        for (int i = 0; i < count; ++i) {
            if (group.getChildAt(i) instanceof TextView) {
                final TextView tv = (TextView) group.getChildAt(i);
                final String text = tv.getText().toString();
                if (text.startsWith("SearchFor")) {
                    metrics = new DisplayMetrics();
                    WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                    wm.getDefaultDisplay().getMetrics(metrics);
                    mScaledDensity = metrics.scaledDensity;

                    if (TEXT_SEARCH_TEXT.equals(text)) {
                        mNotifyTextColor = tv.getTextColors().getDefaultColor();
                        mNotifyTextSize = tv.getTextSize();
                        mNotifyTextSize /= mScaledDensity;
                    } else {
                        mNotifyTitleColor = tv.getTextColors().getDefaultColor();
                        mNotifyTitleSize = tv.getTextSize();
                        mNotifyTitleSize /= mScaledDensity;
                    }

                    if (null != mNotifyTitleColor && mNotifyTextColor != null) {
                        return true;
                    }
                }
            } else if (group.getChildAt(i) instanceof ViewGroup) {
                if (recurseGroup((ViewGroup) group.getChildAt(i))) {
                    return true;
                }
            }
        }
        return false;
    }
 
    private void getPackageResources(String packageName) {
        String resourcesClassName = packageName + ".R";
        String drawableClassName = resourcesClassName + "$drawable";
        String stringClassName = resourcesClassName + "$string";
        String layoutClassName = resourcesClassName + "$layout";

        Context otherAppContext;
        try {
            otherAppContext = createPackageContext(packageName, 
                Context.CONTEXT_INCLUDE_CODE|Context.CONTEXT_IGNORE_SECURITY);
            Resources resources = otherAppContext.getResources();
            Class<?> externalR = otherAppContext.getClassLoader().loadClass(resourcesClassName); 
            Class<?>[] resourceTypes = externalR.getDeclaredClasses();
            for(Class<?> resourceType : resourceTypes ){
                Log.d("RESOURCE TYPE 4s", resourceType.getName());
                for(Field field :  resourceType.getFields()){
                    Log.d("Field Name 4s", field.getName());
                    int id = field.getInt(resourceType);
                    Log.d("id 4s", String.valueOf(id));
                    if(resourceType.getName().equals(stringClassName)){
                        String string = resources.getString(id);
                        Log.d("value 4s", string);
                    }else if(resourceType.getName().equals(drawableClassName)){
                        Drawable drawable = resources.getDrawable(id);
                    }else if(resourceType.getName().equals(layoutClassName)){
                        XmlResourceParser layout = resources.getLayout(id);
                    }
                }
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } 
    }
    
}
