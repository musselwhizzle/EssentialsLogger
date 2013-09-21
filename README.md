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


## License

    Copyright 2012 Joshua Musselwhite

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
