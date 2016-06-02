# Notify4Scripts
Sort of wrapper app that allow to call the notification service and build a notification via shell commands

//I found out that it's very difficult, if not even impossible in some cases, to find informations on which parameters to pass to ActivityManager "am" command in order to have an Android app launched and performing some task as if it is launched by home screen icon
//The best source I found is actually the system laucher database that is located in the data folder of the launcher app.
//For example in the case of a stock Samsung Galaxy Y the database is located in /data/data/com.sec.android.app.twlauncer/databases/launcher.db
//I've been able to view the database using Root Explorer app embedded SQLite database viewer 

//Example of activity that start the execution of one playlist (same as the home screen music playlist shortcut)
//The str_exec string begin with a "/system/bin/sh -" command followed by a newline (same a /system/bin/sh -c") because the "am" (Activity Manager) command is part of the Android shell command interpreter and not a separate executable.

am startservice -e str_exec "/system/bin/sh - \n am start -a android.intent.action.VIEW -t vnd.android.cursor.dir/playlist -e playlist  1 -n com.android.music/.PlaylistBrowserActivity" -e int_id 3 -e str_title "Start Music" -e hex_tcolor "FFaaEE" -e  float_tsize 27 -e float_csize 16 -n com.hal9k.notify4scripts/.NotifyServiceCV

//Example of notification activity that "unfreeze" an app
//option b_execdebug is been enabled and will make the NotifyService

am startservice -e str_exec "su -c \"pm enable com.google.android.apps.maps\"" -e int_id 2 -e str_title "UnFreeze  Maps" -e b_execdebug 1 -n com.hal9k.notify4scripts/.NotifyServiceCV

//Example of notification activity that "freeze" an app
am startservice -e str_exec "su -c \"pm disable com.google.android.apps.maps\"" -e int_id 1 -e str_title "Freeze  Maps" -e b_execdebug 1 -n com.hal9k.notify4scripts/.NotifyServiceCV

pm list packages -d

am startservice -e str_exec "am start -a android.intent.action.MAIN -n com.yahoo.mobile.client.android.im/.YahooMessenger" -e int_id 4 -e str_title "Start YM" -e hex_tcolor "CCaaFF" -e  float_tsize 27 -e float_csize 16 -n com.hal9k.notify4scripts/.NotifyServiceCV

//Terminal Emulator (by Jackpal) is invoked as it would if launched from a Terminal Emulator home screen shortcut. 
//The first command that have to be executed by the NotifyService is the shell as the "am" (Activity Manager) command is part of the Android shell command interpreter and not a separate executable.
//shell can be invoked either with "/system/bin/sh -" for a login Shell or with "/system/bin/sh" for a Non-login Shell.
//Commands output redirection works as usual in true shell
//NOTE: as the command string passed to su is nested into the string passed to Terminal Emulator activity that is nested into string passed to Notify4Service there have been the need to use two different way to escape the double quote character " that sets the start and end of the strings:
//the 1st level nested string is been quoted with use of escaped double quote character \"
//the 2nd level nested string is been quoted with use of URL encoding (as the data passed to Terminal Emulator is actually a URI)
//%22 is equivalent to " 
am startservice -e str_exec "/system/bin/sh \n am start -a jackpal.androidterm.RUN_SCRIPT -d \"file:#echo; echo Working directory is: ;pwd ; echo; sleep 3 ; echo Hello World! ; echo; sleep 3 ; echo su -c %22busybox ps | grep notify4scripts%22  ;  su -c %22busybox ps | grep notify4scripts%22; echo; sleep 5; su -c %22echo This file is been created by Notify4Script NotifyService by invoking a echo output redirection from a Terminal Emulator window > $EXTERNAL_STORAGE/dummyFile.txt%22 && echo && echo Created $EXTERNAL_STORAGE/dummyFile.txt with text content; echo; sleep 3; echo Closing this Terminal window in 5s... ; echo; sleep 3; echo Bye! :^\) ; echo; sleep 2; exit\" -t text/plain -n jackpal.androidterm/.RemoteInterface" -e int_id 4 -e str_title "Hello World!" -e hex_tcolor "000000" -e float_csize 16 -e str_content "Start Terminal Emulator and run few commands with output redirection" -n com.hal9k.notify4scripts/.NotifyServiceCV

//Execute a shell command with su privileges; the command is passed to su at the time su is invoked.
//Use of "su -c [command]" might not work if [command] contain spaces because [command] should be a single parameter, and thus will require quoting.
//Unfortunately both quoting the [command] parameter as well as passing the paramaters as separate variables does not work consistently across all Android versions.
//Commands output redirection isn't possible as the output stream isn't handled by a true shell but is sent to the process's parent that is the NotifyService
am startservice -e str_exec "su -c \"touch $EXTERNAL_STORAGE/dummyFile.txt\"" -e int_id 6 -e str_title "Make a file" -e hex_tcolor "000000"  -e float_csize 16 -e str_content "Create a new empty file in /mnt/sdcard with name dummyFile.txt" -n com.hal9k.notify4scripts/.NotifyServiceCV

//Same as the above example, but in this case the shell command is passed to su after su is been invoked
//Commands output redirection isn't possible as the output stream isn't handled by a true shell but is sent to the process's parent that is the NotifyService
am startservice -e str_exec "su \ntouch $EXTERNAL_STORAGE/dummyFile.txt \ntouch /mnt/sdcard/dummyFile2.txt" -e int_id 6 -e str_title "Make a file" -e hex_tcolor "000000"  -e float_csize 16 -e str_content "Create a new empty file in /mnt/sdcard with name dummyFile.txt" -e b_execdebug 1 -n com.hal9k.notify4scripts/.NotifyServiceCV

//Execute multiple shell commands separated by a new line marker, and collect the output that is then sent to the Log Cat service.
am startservice -e str_exec "su \npm enable com.google.android.apps.maps \necho Google Maps Unfreezed!\n echo Task completed succesfully\necho Have a nice day!" -e int_id 6 -e str_title "Unfreeze Google Maps\nand echo something" -e hex_tcolor "000000"  -e float_csize 16 -e str_content "Test for logging of stderr and stdout streams" -e b_execdebug 1 -n com.hal9k.notify4scripts/.NotifyServiceCV

//Change the wallpaper theme to Hyperion
am startservice -e str_exec "su \ncp -f $EXTERNAL_STORAGE/Pictures/Wallpapers/HyperionGM9_wallpaper_\(copyTo.data_com.android.settings_files\).jpg /data/data/com.android.settings/files/wallpaper ; chmod 770 /data/data/com.android.settings/files/wallpaper ; chown 1000:1000 /data/data/com.android.settings/files/wallpaper ; cp -f $EXTERNAL_STORAGE/Pictures/Wallpapers/HyperionGM9_zzzzzz_lockscreen_wallpaper_\(copyTo.data_com.cooliris.media_files\).jpg /data/data/com.cooliris.media/files/zzzzzz_lockscreen_wallpaper.jpg ; chmod 664 /data/data/com.cooliris.media/files/zzzzzz_lockscreen_wallpaper.jpg ; chown 1000:1000 /data/data/com.cooliris.media/files/zzzzzz_lockscreen_wallpaper.jpg" -n com.hal9k.notify4scripts/.NotifyServiceCV -e str_title "Hyperion Wallpaper" -e str_content "Touch here to set the current home and lockscreen wallpaper to Hyperion theme" -e hex_tcolor "033874" -e float_tsize 20 -e int_iconid 1 -e int_id 1

//Change the wallpaper theme to Aurora
am startservice -e str_exec "su \ncp -f $EXTERNAL_STORAGE/Pictures/Wallpapers/InfectedAurora_wallpaper_\(copyTo.data_com.android.settings_files\).jpg /data/data/com.android.settings/files/wallpaper ; chmod 770 /data/data/com.android.settings/files/wallpaper ; chown 1000:1000 /data/data/com.android.settings/files/wallpaper ; cp -f $EXTERNAL_STORAGE/Pictures/Wallpapers/InfectedAurora_zzzzzz_lockscreen_wallpaper_\(copyTo.data_com.cooliris.media_files\).jpg /data/data/com.cooliris.media/files/zzzzzz_lockscreen_wallpaper.jpg ; chmod 664 /data/data/com.cooliris.media/files/zzzzzz_lockscreen_wallpaper.jpg ; chown 1000:1000 /data/data/com.cooliris.media/files/zzzzzz_lockscreen_wallpaper.jpg" -n com.hal9k.notify4scripts/.NotifyServiceCV -e str_title "Aurora Wallpaper" -e str_content "Touch here to set the current home and lockscreen wallpaper to Aurora theme" -e hex_tcolor "042c15" -e float_tsize 20 -e int_iconid 2 -e int_id 2

//Send a sms
am startservice -e str_exec "su \nservice call isms 6 s16 \"222\" i32 0 i32 0 s16 \"bal\"" -n com.hal9k.notify4scripts/.NotifyServiceCV -e str_title "Bal Inquiry" -e str_content "By touching here a SMS with text \"bal\" will be sent to 222. You'll receive a reply SMS" -e hex_tcolor "94ceef" -e b_noicon 1 -e str_ticker "" -e int_iconid 2 -e int_id 3

//Run a shell script with arguments
//Use of "su -c [command]" instead of "su\n[command]" doesn't work (or it likely might not work) because [command] should be a single parameter, and thus may require quoting.
//Unfortunately both quoting the [command] parameter as well as passing the paramaters as separate variables does not work consistently across all Android versions.
am startservice -e str_exec "su\n/data/scripts_hal9k/keypad_switch.sh -no_notification_update" -e int_id 4 -e str_content "Touch here to switch keypad type" -e hex_ccolor "85c8ff" -e float_csize 19 -e str_title " " -e float_tsize 12 -e b_notime 1 -e b_noicon 1 -e int_iconid 4 -n com.hal9k.notify4scripts/.NotifyServiceCV

//Launching Terminal Emulator and make it start a shell script (dynamic notification content update).
//Having the Terminal Emulator to execute a shell script, instead of having the script executed directly from the notification, allow for automated notification content update.
//An existing notification can be updated by use of notify4scripts service but said service can't be invoked as action from an existing notification
//because it's the notify4scripts service the one who handle the existing notification and will make the call, so it will end up in a loop,
//having the service invoking itself and that's not allowed.
//But if the notification launches Terminal Emulator, as it will run on its own indipendent task, the notify4scripts service can be invoked again
//from commands or scripts launched within Terminal Emulator. 
//NOTE: "su -c [command]"  might not work if [command] contain spaces because [command] should be a single parameter, and thus will require quoting.
//Unfortunately both quoting the [command] parameter as well as passing the paramaters as separate variables does not work consistently across all Android versions.
am startservice -e str_exec "/system/bin/sh \n am start -a jackpal.androidterm.RUN_SCRIPT -d \"file:#su -c /data/scripts_hal9k/keypad_switch.sh ; exit\" -t text/plain -n jackpal.androidterm/.RemoteInterface" -e int_id 4 -e str_title "Touch here to switch keypad type" -e hex_tcolor "85b8ff" -e float_tsize 18 -e float_csize 16 -e str_content "Last keypad type setting was $last_set_keypad" -e b_notime 1 -e b_noicon 1 -e int_iconid 4 -n com.hal9k.notify4scripts/.NotifyServiceCV;
