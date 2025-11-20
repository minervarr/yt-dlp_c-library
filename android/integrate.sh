#!/bin/bash
# Script de integración automática para Android
# Copia todos los archivos necesarios a tu proyecto Android

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  yt-dlp Zoom Android Integration${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# Verificar que estamos en el directorio correcto
if [ ! -f "CMakeLists.txt" ]; then
    echo -e "${RED}Error: Debe ejecutar este script desde el directorio android/${NC}"
    echo "Uso: cd android && ./integrate.sh /path/to/your/android/project"
    exit 1
fi

# Verificar argumento
if [ -z "$1" ]; then
    echo -e "${YELLOW}Uso: ./integrate.sh /path/to/your/android/project${NC}"
    echo ""
    echo "Ejemplo:"
    echo "  ./integrate.sh /home/user/AndroidStudioProjects/MyApp"
    exit 1
fi

PROJECT_DIR="$1"

# Verificar que el directorio existe
if [ ! -d "$PROJECT_DIR" ]; then
    echo -e "${RED}Error: El directorio no existe: ${PROJECT_DIR}${NC}"
    exit 1
fi

# Verificar que es un proyecto Android
if [ ! -f "$PROJECT_DIR/app/build.gradle" ] && [ ! -f "$PROJECT_DIR/app/build.gradle.kts" ]; then
    echo -e "${RED}Error: No parece ser un proyecto Android válido${NC}"
    echo "No se encontró app/build.gradle"
    exit 1
fi

echo -e "${GREEN}✓ Proyecto Android válido encontrado${NC}"
echo ""

# Crear directorios necesarios
echo "Creando estructura de directorios..."
mkdir -p "${PROJECT_DIR}/app/src/main/cpp/include"
mkdir -p "${PROJECT_DIR}/app/src/main/cpp/src"
mkdir -p "${PROJECT_DIR}/app/src/main/cpp/jni"
mkdir -p "${PROJECT_DIR}/app/src/main/java/com/ytdlp/zoom"
mkdir -p "${PROJECT_DIR}/app/src/main/kotlin/com/ytdlp/zoom"

# Copiar archivos C++
echo "Copiando archivos C++..."
cp -r ../include "${PROJECT_DIR}/app/src/main/cpp/"
cp -r ../src "${PROJECT_DIR}/app/src/main/cpp/"
cp -r jni/* "${PROJECT_DIR}/app/src/main/cpp/jni/"

# Copiar CMakeLists.txt
echo "Copiando CMakeLists.txt..."
cp CMakeLists.txt "${PROJECT_DIR}/"

# Copiar archivos Java
echo "Copiando archivos Java..."
cp java/com/ytdlp/zoom/*.java "${PROJECT_DIR}/app/src/main/java/com/ytdlp/zoom/"

# Copiar archivos Kotlin (opcional)
if [ -d "kotlin/com/ytdlp/zoom" ]; then
    echo "Copiando archivos Kotlin..."
    cp kotlin/com/ytdlp/zoom/*.kt "${PROJECT_DIR}/app/src/main/kotlin/com/ytdlp/zoom/"
fi

echo ""
echo -e "${GREEN}✓ Archivos copiados exitosamente${NC}"
echo ""

# Mostrar siguiente paso
echo -e "${YELLOW}═══════════════════════════════════════${NC}"
echo -e "${YELLOW}  SIGUIENTE PASO: Configurar Gradle${NC}"
echo -e "${YELLOW}═══════════════════════════════════════${NC}"
echo ""
echo "1. Edita ${PROJECT_DIR}/app/build.gradle"
echo ""
echo "   Agrega en android { defaultConfig { ... } }:"
echo ""
echo "   ndk {"
echo "       abiFilters 'arm64-v8a', 'armeabi-v7a'"
echo "   }"
echo ""
echo "   externalNativeBuild {"
echo "       cmake {"
echo "           cppFlags \"-std=c++17\""
echo "           arguments \"-DANDROID_STL=c++_shared\""
echo "       }"
echo "   }"
echo ""
echo "   Agrega en android { ... } (fuera de defaultConfig):"
echo ""
echo "   externalNativeBuild {"
echo "       cmake {"
echo "           path file('../CMakeLists.txt')"
echo "           version '3.18.1'"
echo "       }"
echo "   }"
echo ""
echo "2. Agrega en AndroidManifest.xml:"
echo ""
echo "   <uses-permission android:name=\"android.permission.INTERNET\" />"
echo "   <uses-permission android:name=\"android.permission.WRITE_EXTERNAL_STORAGE\" />"
echo "   <uses-permission android:name=\"android.permission.READ_MEDIA_VIDEO\" />"
echo ""
echo "3. Sync Gradle y compila el proyecto"
echo ""
echo -e "${GREEN}Ver documentación completa: android/README.md${NC}"
echo -e "${GREEN}Ver guía rápida: android/QUICKSTART.md${NC}"
echo ""
