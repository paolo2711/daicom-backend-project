# Scripts de Mantenimiento — Daicom Backend

Scripts operativos para **backups** y **restauración** de la base de datos `daicom_demo`,
como parte del plan de mantenimiento de la aplicación.

> Complementan la tarea programada dentro de la app (`MaintenanceTask`, `@Scheduled`),
> que limpia los logs de auditoría antiguos todas las noches.

## 1. Respaldo (`backup_db.bat`)

Genera un dump con marca de tiempo en `../backups/` y elimina los respaldos con más de
14 días (rotación).

```bat
scripts\backup_db.bat
```

Resultado: `backups\daicom_demo_AAAAMMDD_HHMMSS.sql`

Configurable al inicio del `.bat`:
- `DB` — base de datos a respaldar (por defecto `daicom_demo`).
- `RETENTION_DAYS` — días de retención (por defecto 14).
- `MYSQLDUMP` — ruta a `mysqldump.exe` de XAMPP.

## 2. Restauración (`restore_db.bat`)

```bat
scripts\restore_db.bat  ruta\al\respaldo.sql
```

Crea la base si no existe y carga el dump.

## 3. Programar el respaldo automático (cron / Windows)

### Windows — Programador de tareas
1. Abrir **Programador de tareas** → *Crear tarea básica*.
2. Nombre: `Backup Daicom DB`.
3. Desencadenador: **Diariamente**, hora (p. ej. 02:00).
4. Acción: **Iniciar un programa** → Programa/script:
   `H:\ade\integrador\daicom-backend\daicom-backend\scripts\backup_db.bat`
5. Finalizar. (Se ejecutará todos los días y rotará los respaldos.)

### Linux/servidor — cron (equivalente)
```cron
0 2 * * *  /ruta/al/backup_db.sh   # respaldo diario a las 02:00
```

## 4. Buenas prácticas
- Copiar periódicamente la carpeta `backups/` a **otro disco o a la nube** (regla 3-2-1).
- Probar la restauración de vez en cuando (un respaldo no verificado no es un respaldo).
- El motor InnoDB permite respaldo consistente en caliente con `--single-transaction`
  (ya incluido en el script), sin bloquear la aplicación.
