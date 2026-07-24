@echo off
REM ============================================================
REM  Respaldo automatico de la base de datos daicom_demo
REM  - Genera un dump con marca de tiempo
REM  - Rota (elimina) respaldos con mas de RETENTION_DAYS dias
REM
REM  Uso manual:      scripts\backup_db.bat
REM  Uso programado:  ver scripts\README.md (Programador de tareas)
REM ============================================================
setlocal enabledelayedexpansion

REM --- Configuracion ---
set MYSQLDUMP="D:\xampp\mysql\bin\mysqldump.exe"
set DB=daicom_demo
set DBUSER=root
set BACKUP_DIR=%~dp0..\backups
set RETENTION_DAYS=14

REM --- Marca de tiempo yyyymmdd_hhmmss (via PowerShell, robusto en Windows moderno) ---
for /f %%i in ('powershell -NoProfile -Command "Get-Date -Format yyyyMMdd_HHmmss"') do set STAMP=%%i

if not exist "%BACKUP_DIR%" mkdir "%BACKUP_DIR%"
set OUTFILE=%BACKUP_DIR%\%DB%_%STAMP%.sql

echo [%date% %time%] Respaldando %DB% -^> %OUTFILE%
%MYSQLDUMP% -u %DBUSER% --single-transaction --routines --triggers %DB% > "%OUTFILE%"

if %ERRORLEVEL%==0 (
    echo [OK] Respaldo creado: %OUTFILE%
) else (
    echo [ERROR] Fallo el respaldo ^(codigo %ERRORLEVEL%^)
    exit /b %ERRORLEVEL%
)

REM --- Rotacion: eliminar respaldos con mas de RETENTION_DAYS dias ---
forfiles /p "%BACKUP_DIR%" /m "%DB%_*.sql" /d -%RETENTION_DAYS% /c "cmd /c del @path" 2>nul
echo [%date% %time%] Rotacion completada ^(se conservan los ultimos %RETENTION_DAYS% dias^)

endlocal
