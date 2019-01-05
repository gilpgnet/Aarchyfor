@echo off
PATH=C:\AndroidSdk\platform-tools;%PATH%
cd /d %0\..
call adb logcat > logcat.txt
pause