package com.hal9k.notify4scripts;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import android.app.IntentService;
import android.content.Intent;

public class NotificationAction extends IntentService  {

Handler mHandler = null;

	public NotificationAction() {
		super("NotificationAction");
	}
	
	@Override
	public void onCreate() {
	    super.onCreate();
	    mHandler = new Handler();
	}

	@Override
	public void onHandleIntent(Intent intent) {

		if (null != intent) {
			final String str_exec = intent.getStringExtra("str_exec");
			final String b_execdebug = intent.getStringExtra("b_execdebug");

			Log.i("Notify4Scripts - Docommands","NotificationAction, commands to execute: "+str_exec);

			/* Code to display a Toast from a Service found here
			 * from user rony l http://stackoverflow.com/users/16418/rony-l
			 * then commented out because used only for debug purpose
			 * Sending Toast from service require a dedicated coding as in here, otherwise the Toast won't show.
			 */
			if ("1".equals(b_execdebug)) {
				mHandler.post(new Runnable() {            
					@Override
					public void run() {
						//Toast message posted twice to give the user more time to read it
						Toast.makeText(NotificationAction.this, "NotificationAction\n\nCommands to execute: \n"+str_exec+"\n\nNumber of commands: "+str_exec.split("\\\\n").length, Toast.LENGTH_LONG).show();                
						Toast.makeText(NotificationAction.this, "NotificationAction\n\nCommands to execute: \n"+str_exec+"\n\nNumber of commands: "+str_exec.split("\\\\n").length, Toast.LENGTH_LONG).show();                
						Toast.makeText(NotificationAction.this, "For more debug info look at the Notify4Scripts Log messages type I(nfo) and D(ebug)", Toast.LENGTH_LONG).show();                
						Toast.makeText(NotificationAction.this, "For more debug info look at the Notify4Scripts Log messages type I(nfo) and D(ebug)", Toast.LENGTH_LONG).show();                
					}
				});
			}
			
			// The regexp for method .split is been set to "\\\\n" to escape the "\n" character representation
			// that matches newline character in the string and not the pair of "\" followed by a "n".
			docommands(str_exec.split("\\\\n"));
			
		} else {
		Log.d("Notify4Scripts - Docommands","NotificationAction, no intent received");
		}
	}
	

	
	/* The following code is been found first on http://stackoverflow.com/questions/20932102/execute-shell-command-from-android
	 * posted by user 18446744073709551615 http://stackoverflow.com/users/755804/18446744073709551615
	 * but the explanation of the varargs constructor (String... commands) is been found here
	 * http://stackoverflow.com/questions/3158730/java-3-dots-in-parameters
	 * Thanks to Cristian http://stackoverflow.com/users/244296/cristian
	 * to have provided an equivalent "for" loop that makes use of array index
	 * 
	 * The actual code used is a mix of the one mentioned above and the one By Michael C. Daconta
	 * found here http://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html
	 * that provide a proper way to handle the stderr and stdout streams from the executed commands.
	 */   
	public static void docommands(String... commands ) {
		int i = 1;
		
		try{
        	Log.i("Notify4Scripts - Docommands","executing command: "+commands[0]+"\ncommand line number: 0");                
	        Process proc = Runtime.getRuntime().exec(commands[0]);

	        DataOutputStream outputStream = new DataOutputStream(proc.getOutputStream());
	    
            // any error message?
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "stderr");            
            
            // any output?
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "stdout");
                
            // kick them off
            errorGobbler.start();
            outputGobbler.start();
            
	        for (i = 1; i < commands.length; i++) {
	        	Log.i("Notify4Scripts - Docommands","executing command: "+commands[i]+"\ncommand line number: "+i);                

	            outputStream.writeBytes(commands[i]+"\n");
	            outputStream.flush();
	        }
	    
	        outputStream.writeBytes("exit\n");
	        outputStream.flush();

	        try {
	            proc.waitFor();
	        } catch (InterruptedException e) {
	            Log.d("Notify4Scripts - Docommands","catched InterruptedException while trying su.waitFor()");
	        }
	        outputStream.close();
	        
	    } catch(IOException e){
	        Log.d("Notify4Scripts - Docommands","catched IOException while trying to execute the following commands: "+commands[i]);
	    }
	}

	// Code found here http://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html
	static class StreamGobbler extends Thread
	{
	    InputStream is;
	    String type;
		StringBuffer output = new StringBuffer();
    
	    StreamGobbler(InputStream is, String type)
	    {
	        this.is = is;
	        this.type = type;
	    }
	    
	    public void run()
	    {
	        try
	        {
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            String line=null;
	            int i = 0;

	            while ( (line = br.readLine()) != null) {
	            	i++ ;
	            	output.append(" .line"+ (i < 9 ? "0"+i : i) +">" + line + "\n");
	            }
	        } catch (IOException e) {
	    	        Log.d("Notify4Scripts - Docommands","catched IOException while trying to read command output of type \""+type+"\"");
	        }
	    // Strange enough Log Cat doesn't display empty newline and it treat the 1st line
	    // of log content as empty so it ignore the "\n" added at the end.
	    // As a workaround I found by myself that adding a "\t\n" does the trick
        Log.d("Notify4Scripts - Docommands","Commands output for \""+type+"\" stream:\n"+output.toString());
	    }
	}
}	    
	