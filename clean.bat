@echo off
PATH=C:\Program Files\Java\jdk1.8.0_191\bin;%PATH%
cd /d %0\..
call gradlew clean
del logcat.txt
del gradlew
del gradlew.bat
rmdir /S /Q .gradle
rmdir /S /Q gradle
pause