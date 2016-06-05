# Notify4Scripts
Sort of wrapper app that allows to call the notification service and build, trigger, customize notifications via Android shell commands

As shell commands can be given to an Android device via adb and a computer adb server can be connected to a device over WiFi network, Notify4Scripts allows to build and display notifications on a target phone, manually and programmatically from a remote computer.

Notify4Scripts has two service classes: NotifyService and NotifyServiceCV  
The difference among the two is that NotifyServiceCV supports CustomView notifications and a number of parameters to customize the notification, while NotifyService only support standard basic notification.

See the Wiki for more infos about usage and some examples.
