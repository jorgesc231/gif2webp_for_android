# Port de gif2webp a Android

gif2webp es una utilidad escrita en C que permite convertir imagenes en formato gif a formato webp animado:

https://developers.google.com/speed/webp/docs/gif2webp

En este proyecto se compila junto con sus dependencias (libgif y libwebp) para ser usada por aplicaciones Android.

### App Demo

Incluye una app demo que permite descargar un gif de internet y convertirlo a webp, también muestra el tiempo que demoro la conversión y la comparación de tamaño de los archivos.

No soy desarrollador Android a si que el código es bastante malo y no comprueba todos los posibles errores.
(Usar con precaución...)


## Requisitos

- Android Studio

- Instalar CMake

      Android Studio -> SDK Manager -> SDK Tools -> Cmake -> Seleccionar la version 3.22.1 o superior

- Instalar NDK 

      Android Studio -> SDK Manager -> SDK Tools -> NDK (Side by side) -> 25.1.8937393
  
  (Puede ser cualquier version superior a la 22 pero es necesario cambiarlo en el build.gradle)

- Instalar SDK 

      Android Studio -> SDK Manager -> SDK Platforms -> Instalar la ultima version disponible y otra que sea mayor o igual a Android 6.


## Compilar

1. Clonar el repositorio:


    git clone https://github.com/jorgesc231/gif2webp_for_android
  

2. Abrir con Android Studio y Compilar el proyecto.

3. Ejecutar la app demo.


    Compilar en Release duplica o triplica el rendimiento.

    Build -> Select Build Variant... -> Active Build Variant -> Seleccionar "release" y volver a compilar


## Instrucciones para usar en una app Android

TODO

Obtener el archivo libgif2webp.so e incluirlo en el proyecto usando CMake...


## Por Hacer

- Una mejor interfaz con el codigo Java
- Ejecutar gif2web en otro thread que no sea el de la UI
- Instrucciones de uso en una app Android
