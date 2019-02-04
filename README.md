# Aarchyfor
Manejo de archivos y foráneas en Android. Es un front end para https://github.com/gilpgnet/archyfor.

Para entender mejor el código, se recomienda estudiar primero los proyectos:
- https://github.com/gilpgnet/AideMens,
- https://github.com/gilpgnet/AideSal y
- https://github.com/gilpgnet/Asincro.

## Ejecución en Windows
1. Abre el Símbolo del Sistema (cmd).
2. Prepara el dispositivo para ejecutar la app. Aquí se plantean 3 opciones:
   * [**Emulador Memu.**](https://www.memuplay.com/) Levanta el emulador. En los **Ajustes**, **Opciones de Desarrollo**,
     verifica que la **Depuración USB** esté activada. Desde una terminal teclea el siguiente comando.
  
     ```Batchfile
        call adb connect 127.0.0.1:21503
     ```
  
   * **Emulador normal.**  Levanta el emulador y desde una terminal teclea el siguiente comando.
  
     ```Batchfile
        call adb connect 127.0.0.1
     ```
  
   * **Dispositivo externo en modo desarrollador.** Conecta el dispositivo por USB a tu computadora. Si usas Windows 10, está listo.
     Si es una versión menor, necesitas descargar el driver para tu teléfono del sitio del fabricante, instalarlo y luego conectar tu
     dispositivo.
  
3. Cámbiate a la carpeta del ejemplo descompactado. Por ejemplo, si descompactas el proyecto en la carpeta
   _C:\ejemplos_, introduce el comando
   ```Batchfile
        cd C:\ejemplos\Aarchyfor-master
   ```
4. Prepara el proyecto para usar gradle. Ejecuta el comando
   ```Batchfile
        call gradle wrapper
   ```
   Se crean los archivos
   - carpeta .gradle
   - carpeta gradle
   - gradlew
   - gradlew.bat  
5. Para ejecutar el código, ejecuta el comando
   ```Batchfile
        call gradlew installDebug
   ```
   Compila el código y lo instala en el móvil. Solo tienes que hacer clic en el ícono instalado en el dispositivo.
  
6. Si la aplicación aborta inesperadamente, introduce el siguiente comando
   ```Batchfile
        call adb logcat > logcat.txt
   ```
   
   Revisa el contenido del archivo _logcat.txt_. Es algo largo, pero revísalo desde el final hacia el inicio.
7. Para eliminar los archivos innecesarios después de trabajar, ejecuta el comando
   ```Batchfile
        call clean
   ```
   Para volver a usar el proyecto, repite a partir del paso 5.
