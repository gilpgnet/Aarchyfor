@echo off
rem Configura el proyecto para usar Gradle y crea los archivos:
rem carpeta .gradle
rem carpeta gradle
rem gradlew
rem gradlew.bat
PATH=C:\Program Files\Java\jdk1.8.0_191\bin;%PATH%
PATH=C:\gradle-4.9\bin;%PATH%
cd /d %0\..
call gradle wrapper
pause