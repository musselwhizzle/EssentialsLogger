EssentialsLogger
================

A utility for logging on Android. 

This logger wraps the native Android Log and extends it functionality by adding features such as allowing you to set the log level at runtime. You can also control where the logs end up by adding different logger types

Logger Types
================

1) DatabaseLogger - sends the log calls to an sqlite data base

2) ErrorReportNotificationBarLogger - when a crash happens, sends the log call to the notification bar allowing users to email the output. 

3) FileLogger - sends the log calls to a file 

4) LogCatLogger - a wrapper for the standard Log class

5) LoggerServiceRemoteProxy - allows you to communicate with a Logger running in a different process 
