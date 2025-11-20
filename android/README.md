# yt-dlp Zoom Downloader para Android

Librería Android (C++ con JNI) para descargar grabaciones de Zoom usando la API nativa.

## ✨ Características

- ✅ Descarga grabaciones de Zoom directamente desde Android
- ✅ Soporte para autenticación con cookies
- ✅ API en Java y Kotlin
- ✅ Soporte para coroutines de Kotlin
- ✅ Extracción de metadatos (título, duración, tamaño)
- ✅ Compatible con Android 7.0+ (API 24+)
- ✅ Soporta múltiples arquitecturas (arm64-v8a, armeabi-v7a, x86, x86_64)

## 📋 Requisitos Previos

- **Android Studio**: Arctic Fox o superior
- **NDK**: r21 o superior (se instala automáticamente)
- **CMake**: 3.18.1 o superior (se instala automáticamente)
- **Gradle**: 7.0 o superior
- **Dependencias nativas**:
  - libcurl (incluida en Android)
  - OpenSSL (incluida en Android)

## 🚀 Instalación

### Opción 1: Integración Directa en tu Proyecto

#### Paso 1: Copiar archivos a tu proyecto

```bash
# Estructura de tu proyecto Android
YourApp/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/ytdlp/zoom/  # <-- Copiar archivos Java aquí
│   │       ├── cpp/                  # <-- Copiar archivos JNI aquí
│   │       └── AndroidManifest.xml
│   └── build.gradle
└── CMakeLists.txt                    # <-- Copiar CMakeLists.txt aquí
```

Copiar estos archivos:
```bash
# Desde el directorio android/ de este repo:
cp -r java/com/ytdlp/zoom YourApp/app/src/main/java/com/ytdlp/
cp -r kotlin/com/ytdlp/zoom YourApp/app/src/main/kotlin/com/ytdlp/  # Opcional
cp -r jni/* YourApp/app/src/main/cpp/
cp CMakeLists.txt YourApp/
```

#### Paso 2: Configurar build.gradle

Edita `app/build.gradle`:

```gradle
android {
    // ... otras configuraciones ...

    defaultConfig {
        // ... otras configuraciones ...

        ndk {
            abiFilters 'arm64-v8a', 'armeabi-v7a'  // O todas: 'x86', 'x86_64'
        }

        externalNativeBuild {
            cmake {
                cppFlags "-std=c++17"
                arguments "-DANDROID_STL=c++_shared"
            }
        }
    }

    externalNativeBuild {
        cmake {
            path file('../CMakeLists.txt')
            version '3.18.1'
        }
    }
}

dependencies {
    // Coroutines (si usas Kotlin)
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
}
```

#### Paso 3: Agregar permisos en AndroidManifest.xml

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
                     android:maxSdkVersion="28" />
    <uses-permission android:name="android.permission.READ_MEDIA_VIDEO" />
    <!-- ... -->
</manifest>
```

### Opción 2: Usar el Proyecto de Ejemplo

```bash
# Abrir en Android Studio
cd android/example
# File > Open > Seleccionar carpeta 'example'
```

## 📖 Uso

### Inicialización

```kotlin
// Kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var downloader: ZoomDownloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar downloader
        downloader = ZoomDownloader(this)
    }
}
```

```java
// Java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializar librería
        YtdlpNative.initialize();
    }
}
```

### Verificar URL

```kotlin
// Kotlin
val url = "https://utec.zoom.us/rec/play/xxx..."
val isValid = downloader.isValidUrl(url)
if (isValid) {
    val videoId = downloader.extractVideoId(url)
    Log.i(TAG, "Video ID: $videoId")
}
```

```java
// Java
String url = "https://utec.zoom.us/rec/play/xxx...";
boolean isValid = ZoomExtractor.isValidUrl(url);
if (isValid) {
    String videoId = ZoomExtractor.extractVideoId(url);
    Log.i(TAG, "Video ID: " + videoId);
}
```

### Obtener Información del Video

```kotlin
// Kotlin con Coroutines
lifecycleScope.launch {
    val url = "https://utec.zoom.us/rec/play/xxx..."
    val cookiesFile = File(getExternalFilesDir(null), "cookies.txt")

    val videoInfo = downloader.getVideoInfo(url, cookiesFile)

    if (videoInfo != null) {
        Log.i(TAG, "Título: ${videoInfo.title}")
        Log.i(TAG, "Duración: ${videoInfo.formatDuration()}")
        Log.i(TAG, "Tamaño: ${videoInfo.fileSize} MB")
    }
}
```

```java
// Java
String url = "https://utec.zoom.us/rec/play/xxx...";
String cookiesPath = new File(getExternalFilesDir(null), "cookies.txt").getAbsolutePath();

ZoomExtractor.VideoInfo info = ZoomExtractor.getVideoInfo(url, cookiesPath);
if (info != null) {
    Log.i(TAG, "Título: " + info.title);
    Log.i(TAG, "Duración: " + info.duration + " segundos");
    Log.i(TAG, "Tamaño: " + info.fileSize + " MB");
}
```

### Descargar Video

```kotlin
// Kotlin con Coroutines
lifecycleScope.launch {
    val url = "https://utec.zoom.us/rec/play/xxx..."

    val downloadsDir = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOWNLOADS
    )
    val outputFile = File(downloadsDir, "video.mp4")
    val cookiesFile = File(getExternalFilesDir(null), "cookies.txt")

    val result = downloader.downloadVideo(
        url = url,
        outputFile = outputFile,
        cookiesFile = if (cookiesFile.exists()) cookiesFile else null,
        onProgress = { progress ->
            Log.d(TAG, "Progreso: ${(progress * 100).toInt()}%")
        }
    )

    if (result.isSuccess) {
        val file = result.getOrNull()
        Toast.makeText(this@MainActivity,
            "Descarga completa: ${file?.absolutePath}",
            Toast.LENGTH_LONG).show()
    } else {
        Toast.makeText(this@MainActivity,
            "Error: ${result.exceptionOrNull()?.message}",
            Toast.LENGTH_LONG).show()
    }
}
```

```java
// Java
String url = "https://utec.zoom.us/rec/play/xxx...";
File outputFile = new File(
    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
    "video.mp4"
);
String cookiesPath = new File(getExternalFilesDir(null), "cookies.txt").getAbsolutePath();

boolean success = ZoomExtractor.download(url, outputFile.getAbsolutePath(), cookiesPath);
if (success) {
    Toast.makeText(this, "Descarga completa!", Toast.LENGTH_LONG).show();
} else {
    Toast.makeText(this, "Error al descargar", Toast.LENGTH_LONG).show();
}
```

## 🍪 Autenticación con Cookies

Para descargar videos privados o con autenticación:

### Paso 1: Exportar cookies desde el navegador

Usando una extensión como "Get cookies.txt LOCALLY":
1. Ir a zoom.us e iniciar sesión
2. Exportar cookies en formato Netscape
3. Guardar como `cookies.txt`

### Paso 2: Copiar archivo al dispositivo

```bash
# Usando ADB
adb push cookies.txt /sdcard/Download/cookies.txt
```

### Paso 3: Usar en la app

```kotlin
val cookiesFile = File(
    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
    "cookies.txt"
)

if (cookiesFile.exists()) {
    val videoInfo = downloader.getVideoInfo(url, cookiesFile)
}
```

## 🏗️ Arquitectura

```
android/
├── CMakeLists.txt              # Configuración CMake para NDK
├── jni/                        # Código JNI (C++)
│   ├── ytdlp_jni.cpp          # JNI básico
│   └── zoom_extractor_jni.cpp # JNI para Zoom extractor
├── java/com/ytdlp/zoom/       # Interfaz Java
│   ├── YtdlpNative.java       # Clase nativa base
│   └── ZoomExtractor.java     # Extractor de Zoom (Java)
├── kotlin/com/ytdlp/zoom/     # Interfaz Kotlin (opcional)
│   └── ZoomDownloader.kt      # Wrapper con coroutines
└── example/                    # App de ejemplo
    ├── MainActivity.kt
    ├── build.gradle
    └── AndroidManifest.xml
```

## 🔧 Compilación Manual

Si necesitas compilar manualmente la librería nativa:

```bash
# Configurar variables
export ANDROID_NDK_HOME=/path/to/ndk
export ANDROID_ABI=arm64-v8a  # o armeabi-v7a, x86, x86_64

# Compilar
cd android
mkdir -p build/${ANDROID_ABI}
cd build/${ANDROID_ABI}

cmake ../.. \
    -DCMAKE_TOOLCHAIN_FILE=${ANDROID_NDK_HOME}/build/cmake/android.toolchain.cmake \
    -DANDROID_ABI=${ANDROID_ABI} \
    -DANDROID_PLATFORM=android-24 \
    -DANDROID_STL=c++_shared

cmake --build .
```

## 📱 Compatibilidad

- **Versiones de Android**: 7.0 (API 24) - 14.0 (API 34+)
- **Arquitecturas**: arm64-v8a, armeabi-v7a, x86, x86_64
- **Lenguajes**: Java, Kotlin

## 🐛 Solución de Problemas

### Error: "Library not found: ytdlp-zoom"

Verifica que CMakeLists.txt esté configurado correctamente en `build.gradle`:

```gradle
externalNativeBuild {
    cmake {
        path file('../CMakeLists.txt')  // Ruta correcta
    }
}
```

### Error: "UnsatisfiedLinkError"

1. Verifica que las arquitecturas ABI estén incluidas:
```gradle
ndk {
    abiFilters 'arm64-v8a', 'armeabi-v7a'
}
```

2. Limpia y reconstruye:
```bash
./gradlew clean
./gradlew assembleDebug
```

### Error de permisos en Android 13+

Solicita permisos dinámicamente:

```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    requestPermissions(
        arrayOf(Manifest.permission.READ_MEDIA_VIDEO),
        PERMISSION_REQUEST_CODE
    )
}
```

### Video no descarga (error 403/401)

Asegúrate de:
1. Usar cookies válidas y actualizadas
2. El archivo de cookies está en formato Netscape correcto
3. La sesión de cookies no ha expirado

## 📄 API Reference

### ZoomDownloader (Kotlin)

```kotlin
class ZoomDownloader(context: Context) {
    fun isValidUrl(url: String): Boolean
    suspend fun getVideoInfo(url: String, cookiesFile: File?): VideoInfo?
    suspend fun downloadVideo(url: String, outputFile: File, cookiesFile: File?, onProgress: ((Float) -> Unit)?): Result<File>
    fun extractVideoId(url: String): String?
}
```

### ZoomExtractor (Java)

```java
public class ZoomExtractor {
    public static boolean isValidUrl(String url)
    public static String extractVideoId(String url)
    public static String extractInfo(String url, String cookiesFile)
    public static boolean downloadVideo(String url, String outputPath, String cookiesFile, Object progressCallback)
    public static VideoInfo getVideoInfo(String url, String cookiesFile)
}
```

### VideoInfo

```kotlin
data class VideoInfo(
    val videoId: String,
    val title: String,
    val duration: Int,           // segundos
    val downloadUrl: String,
    val chapterUrl: String?,
    val fileSize: Long,          // MB
    val meetingTopic: String,
    val meetingStartTime: String?,
    val hasTranscript: Boolean
)
```

## 📝 Ejemplo Completo

Ver el proyecto completo en `android/example/MainActivity.kt`

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:
1. Fork el repositorio
2. Crea una rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit tus cambios (`git commit -am 'Agrega nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crea un Pull Request

## 📜 Licencia

Este proyecto está bajo la licencia del proyecto original yt-dlp.

## 🙏 Agradecimientos

- Basado en [yt-dlp](https://github.com/yt-dlp/yt-dlp)
- Utiliza libcurl y OpenSSL

---

**Nota**: Esta librería está diseñada para uso personal y educativo. Respeta los términos de servicio de Zoom y las leyes de copyright aplicables.
