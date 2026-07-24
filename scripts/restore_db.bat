@echo off
REM ============================================================
REM  Restauracion de la base de datos daicom_demo desde un dump
REM
REM  Uso:  scripts\restore_db.bat  ruta\al\respaldo.sql
REM ============================================================
setlocal

set MYSQL="D:\xampp\mysql\bin\mysql.exe"
set DB=daicom_demo
set DBUSER=root

if "%~1"=="" (
    echo Uso: restore_db.bat ruta\al\respaldo.sql
    exit /b 1
)
if not exist "%~1" (
    echo [ERROR] No existe el archivo: %~1
    exit /b 1
)

echo Restaurando %DB% desde "%~1" ...
%MYSQL% -u %DBUSER% -e "CREATE DATABASE IF NOT EXISTS %DB% CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
%MYSQL% -u %DBUSER% %DB% < "%~1"

if %ERRORLEVEL%==0 (
    echo [OK] Restauracion completada en la base %DB%
) else (
    echo [ERROR] Fallo la restauracion ^(codigo %ERRORLEVEL%^)
    exit /b %ERRORLEVEL%
)

endlocal
