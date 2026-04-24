#!/bin/bash

# =========================
# CONFIGURACIÓN
# =========================
REMOTE_USER="dietpi"
REMOTE_HOST="192.168.2.2"
REMOTE_PATH="/home/dietpi/cafedemo"

# Nombre del jar (ajustalo si cambia)
JAR_NAME="cafeDemo-0.0.1-SNAPSHOT.jar"

# Carpeta target de Maven
TARGET_DIR="target"

# =========================
# COMPILAR PROYECTO
# =========================
echo "Compilando proyecto con Maven..."
mvn clean install

if [ $? -ne 0 ]; then
    echo "❌ Error al compilar con Maven"
    exit 1
fi

echo "✅ Compilación exitosa"

# =========================
# VERIFICAR JAR
# =========================
JAR_PATH="$TARGET_DIR/$JAR_NAME"

if [ ! -f "$JAR_PATH" ]; then
    echo "❌ No se encontró el JAR en $JAR_PATH"
    exit 1
fi

# =========================
# COPIAR A SERVIDOR
# =========================
echo "Copiando JAR a $REMOTE_USER@$REMOTE_HOST:$REMOTE_PATH ..."

scp "$JAR_PATH" "$REMOTE_USER@$REMOTE_HOST:$REMOTE_PATH"

if [ $? -ne 0 ]; then
    echo "❌ Error al copiar el archivo"
    exit 1
fi

echo "✅ JAR copiado correctamente a la Raspberry"
# =========================
# REINICIAR SERVICIO REMOTO
# =========================
echo "🔄 Reiniciando servicio cafedemo..."

ssh "$REMOTE_USER@$REMOTE_HOST" "sudo systemctl restart cafedemo"

if [ $? -ne 0 ]; then
    echo "❌ Error al reiniciar el servicio"
    exit 1
fi

echo "✅ Servicio reiniciado correctamente"

# =========================
# FIN
# =========================
echo "🎉 Deploy completo"
