package com.hal9k.notify4scripts;

import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;

public class Notify extends Activity {
	Button Start;
	TextView launcherDBtextView;
		    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		launcherDBtextView = (TextView)findViewById(R.id.launcherDBcontent); 

	    launcherDBtextView.setOnLongClickListener(new View.OnLongClickListener(){

			@Override @SuppressLint("NewApi") @SuppressWarnings("deprecation")
	    	public boolean onLongClick(View arg0) {

	    		
	    		//Code for copying text to clipboard found here
	            //http://stackoverflow.com/questions/10385456/cross-version-starting-api-7-eclair-copy-paste-in-android
	            if (android.os.Build.VERSION.SDK_INT < 11)
	            {
	                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
	                if (clipboard != null)
	                {
	                    clipboard.setText(launcherDBtextView.getText().toString());
	                }
	            }
	            else
	            {
	                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
	                if (clipboard != null)
	                {
	                    android.content.ClipData clip = android.content.ClipData.newPlainText("launcherDBcontent", launcherDBtextView.getText().toString());
	                    clipboard.setPrimaryClip(clip);
	                }
	            }
	            
	            Toast.makeText(Notify.this, "Content copied to clipboard", Toast.LENGTH_SHORT).show();
	    		return false;
	    	}
	    });
	    
	    Start = (Button)findViewById(R.id.start);

	    Start.setOnClickListener(new View.OnClickListener() {
	    	@Override
	    	public void onClick(View arg0) {
	
	    		// The following code for reading the content of launcher.db is been found
	    		// here http://tonmoy-paul.blogspot.com/2011/06/how-to-read-launcherdb.html
	    		final ContentResolver cr = getContentResolver();
	    		final String AUTHORITY = getAuthorityFromPermission(Notify.this,"com.android.launcher.permission.READ_SETTINGS");
	    		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");

	    		String DBcontent_text = "";
	    		
	    		if (AUTHORITY != null) {
	    			
	    			Cursor cursor = cr.query(CONTENT_URI,null,null,null,null);
	    			cursor.moveToFirst();
	
	    			do {
	    				DBcontent_text = DBcontent_text +
	    				"id = " + cursor.getString(cursor.getColumnIndex("_id")) + "\n" +
	    				"title = " + cursor.getString(cursor.getColumnIndex("title")) + "\n" +
	    				"intent = " + cursor.getString(cursor.getColumnIndex("intent")) + "\n" + 
	    				"appWidgetId = " + Integer.toString(cursor.getInt(cursor.getColumnIndex("appWidgetId"))) + "\n\n"
	    				;

	    			} while (cursor.moveToNext());
	    		    
	    			launcherDBtextView.setText("Launcher DB read provider is:\n"+AUTHORITY+"\n\n"+DBcontent_text);
				
	    		} else {
	    			launcherDBtextView.setText("Sorry, can't read launcer DB.\n\n" +
	    					"Can't find any authority provider that " +
	    					"declares this permission:\ncom.android.launcher.permission.READ_SETTINGS");
	     		}
	    	}

	    	// Code of this function found here http://stackoverflow.com/questions/8501306/android-shortcut-access-launcher-db
	    	// from user FredG http://stackoverflow.com/users/1444910/fredg
	    	String getAuthorityFromPermission(Context context, String permission) {
	    	    if (permission == null) return null;
	    	    List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
	    	    if (packs != null) {
	    	        for (PackageInfo pack : packs) { 
	    	            ProviderInfo[] providers = pack.providers; 
	    	            if (providers != null) { 
	    	                for (ProviderInfo provider : providers) { 
	    	                    if (permission.equals(provider.readPermission)) return provider.authority;
	    	                    if (permission.equals(provider.writePermission)) return provider.authority;
	    	                } 
	    	            }
	    	        }
	    	    }
	    	    return null;
	    	}
	    });

	}
   
}
