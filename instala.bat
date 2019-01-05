@echo off
PATH=C:\Program Files\Java\jdk1.8.0_191\bin;%PATH%
cd /d %0\..
call gradlew installDebug
pause