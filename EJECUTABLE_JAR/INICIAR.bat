@echo off
title DAICOM
chcp 65001 >nul
setlocal

echo ============================================================
echo   PROYECTO DAICOM
echo ============================================================
echo.
echo   Antes de iniciar por primera vez, importe la base de datos
echo   daicom_demo.sql en MySQL/MariaDB (XAMPP).
echo.

REM Verificar Java
where java >nul 2>nul
if errorlevel 1 (
  echo [ERROR] No se encontro Java. Instale Java 17 y vuelva a intentar.
  pause
  exit /b 1
)

REM Preguntar el puerto (Enter = 8080)
set "PUERTO=8080"
set /p PUERTO=Puerto en el que abrir la aplicacion [Enter = 8080]:

REM Avisar si el puerto ya esta en uso
netstat -ano | find "LISTENING" | find ":%PUERTO% " >nul
if not errorlevel 1 (
  echo.
  echo [AVISO] El puerto %PUERTO% ya esta en uso. Elija otro o cierre lo que lo use.
  echo.
  pause
)

echo.
echo   Iniciando en el puerto %PUERTO% ...
echo   El navegador se abrira solo. No cierre esta ventana mientras use la app.
echo.

REM Abrir el navegador cuando el servidor este arriba
start "" cmd /c "timeout /t 12 /nobreak >nul & start http://localhost:%PUERTO%"

REM Arrancar la aplicacion (backend + web juntos) en el puerto elegido
java -jar DAICOM.jar --server.port=%PUERTO%

echo.
echo La aplicacion se detuvo. Puede cerrar esta ventana.
pause
