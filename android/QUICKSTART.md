# 🚀 Inicio Rápido - 5 Minutos

Guía ultra rápida para integrar la librería en tu proyecto Android existente.

## ⚡ Integración Rápida

### 1. Copiar archivos (30 segundos)

```bash
# Desde la raíz del proyecto yt-dlp_c-library
cd android

# Copiar a TU proyecto (reemplaza /path/to/YourApp)
PROJECT_DIR="/path/to/YourApp"

# Copiar fuentes C++
cp -r ../include ${PROJECT_DIR}/app/src/main/cpp/
cp -r ../src ${PROJECT_DIR}/app/src/main/cpp/
cp -r jni ${PROJECT_DIR}/app/src/main/cpp/

# Copiar fuentes Java/Kotlin
cp -r java/com ${PROJECT_DIR}/app/src/main/java/
cp -r kotlin/com ${PROJECT_DIR}/app/src/main/kotlin/  # Opcional

# Copiar CMakeLists.txt
cp CMakeLists.txt ${PROJECT_DIR}/
```

### 2. Configurar build.gradle (1 minuto)

Edita `app/build.gradle` y agrega:

```gradle
android {
    // ... configuración existente ...

    defaultConfig {
        // ... configuración existente ...

        ndk {
            abiFilters 'arm64-v8a', 'armeabi-v7a'
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
    // ... dependencias existentes ...
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'
}
```

### 3. Agregar permisos en AndroidManifest.xml (30 segundos)

```xml
<manifest>
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
                     android:maxSdkVersion="28" />
    <uses-permission android:name="android.permission.READ_MEDIA_VIDEO" />

    <application android:usesCleartextTraffic="true">
        <!-- ... -->
    </application>
</manifest>
```

### 4. Usar en tu Activity (2 minutos)

#### Kotlin

```kotlin
import com.ytdlp.zoom.ZoomDownloader
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var downloader: ZoomDownloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        downloader = ZoomDownloader(this)

        // Descargar video
        lifecycleScope.launch {
            val url = "https://utec.zoom.us/rec/play/xxx..."
            val outputFile = File(getExternalFilesDir(null), "video.mp4")

            val result = downloader.downloadVideo(url, outputFile)
            if (result.isSuccess) {
                Toast.makeText(this@MainActivity, "¡Descarga completa!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
```

#### Java

```java
import com.ytdlp.zoom.ZoomExtractor;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(() -> {
            String url = "https://utec.zoom.us/rec/play/xxx...";
            File outputFile = new File(getExternalFilesDir(null), "video.mp4");

            boolean success = ZoomExtractor.download(url, outputFile.getAbsolutePath(), null);

            runOnUiThread(() -> {
                if (success) {
                    Toast.makeText(this, "¡Descarga completa!", Toast.LENGTH_LONG).show()
                }
            });
        }).start();
    }
}
```

### 5. Compilar y ejecutar

```bash
./gradlew assembleDebug
# O desde Android Studio: Run > Run 'app'
```

## 🍪 Autenticación (Opcional)

Si necesitas descargar videos privados:

```kotlin
// 1. Exportar cookies desde Chrome usando extensión "Get cookies.txt LOCALLY"
// 2. Copiar archivo al dispositivo:
//    adb push cookies.txt /sdcard/Download/cookies.txt

val cookiesFile = File(
    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
    "cookies.txt"
)

lifecycleScope.launch {
    val result = downloader.downloadVideo(
        url = url,
        outputFile = outputFile,
        cookiesFile = if (cookiesFile.exists()) cookiesFile else null
    )
}
```

## 📱 Ejemplo Completo Mínimo

### MainActivity.kt (Versión mínima funcional)

```kotlin
package com.example.myapp

import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ytdlp.zoom.ZoomDownloader
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val downloader = ZoomDownloader(this)

        findViewById<Button>(R.id.downloadButton).setOnClickListener {
            lifecycleScope.launch {
                val url = "https://utec.zoom.us/rec/play/tu-url-aqui"
                val outputFile = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "zoom_video.mp4"
                )

                Toast.makeText(this@MainActivity, "Descargando...", Toast.LENGTH_SHORT).show()

                val result = downloader.downloadVideo(url, outputFile)

                if (result.isSuccess) {
                    Toast.makeText(
                        this@MainActivity,
                        "Descargado en: ${outputFile.absolutePath}",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Error: ${result.exceptionOrNull()?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
```

### activity_main.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical"
    android:padding="16dp">

    <Button
        android:id="@+id/downloadButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Descargar Video de Zoom" />

</LinearLayout>
```

## ✅ Checklist de Verificación

- [ ] CMakeLists.txt copiado a la raíz del proyecto
- [ ] Archivos Java copiados a `app/src/main/java/com/ytdlp/zoom/`
- [ ] Archivos C++ copiados a `app/src/main/cpp/`
- [ ] `build.gradle` configurado con NDK y CMake
- [ ] Permisos agregados en `AndroidManifest.xml`
- [ ] Sync Gradle completado sin errores
- [ ] Proyecto compila correctamente

## 🐛 Errores Comunes

**Error: "Library not found"**
```bash
# Solución: Limpiar y reconstruir
./gradlew clean
./gradlew assembleDebug
```

**Error: "CMakeLists.txt not found"**
```gradle
// Verificar ruta en build.gradle
externalNativeBuild {
    cmake {
        path file('../CMakeLists.txt')  // Debe apuntar a la raíz del proyecto
    }
}
```

**Error de permisos**
```kotlin
// Solicitar permisos en tiempo de ejecución (Android 6+)
ActivityCompat.requestPermissions(this,
    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
    100)
```

## 📚 Siguientes Pasos

- Ver documentación completa: [README.md](README.md)
- Ver ejemplos avanzados: [example/MainActivity.kt](example/MainActivity.kt)
- API Reference completa en README.md

---

¿Problemas? Revisa la [sección de troubleshooting](README.md#-solución-de-problemas) en el README.
