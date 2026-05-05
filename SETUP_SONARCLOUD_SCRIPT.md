# Script de Setup para SonarCloud

Este script automatiza la configuración de SonarCloud en nuevos repositorios.

## Uso Rápido

```powershell
# Desde el directorio raíz del nuevo repo
.\setup-sonarcloud.ps1 -ProjectKey "mi-proyecto-key" -ProjectName "Mi Proyecto"
```

## Parámetros

- **ProjectKey** (obligatorio): Identificador único en SonarCloud, ej: `auth-service`, `gateway-api`, etc.
- **ProjectName** (obligatorio): Nombre visible del proyecto, ej: `Auth Service`, `Gateway API`, etc.
- **Organization** (opcional): Clave de organización en SonarCloud. Por defecto: `fabrica-escuela`

## Ejemplos

### Configurar un nuevo repo de autenticación
```powershell
.\setup-sonarcloud.ps1 -ProjectKey "auth-service" -ProjectName "Authentication Service"
```

### Configurar un gateway
```powershell
.\setup-sonarcloud.ps1 -ProjectKey "api-gateway" -ProjectName "API Gateway"
```

### Especificar organización diferente
```powershell
.\setup-sonarcloud.ps1 -ProjectKey "test-project" -ProjectName "Test Project" -Organization "mi-otra-org"
```

## Lo que hace el script

1. ✓ Crea el directorio `.github/workflows/` si no existe
2. ✓ Genera `sonar-project.properties` con la configuración del proyecto
3. ✓ Genera `.github/workflows/sonarcloud.yml` con el workflow de GitHub Actions
4. ✓ Actualiza `pom.xml` (si existe) con las propiedades de Sonar

## Después de ejecutar el script

```bash
# 1. Revisar que todo esté configurado correctamente
git status

# 2. Crear rama para el cambio
git checkout -b feature/sonarcloud-config

# 3. Agregar todos los cambios
git add .

# 4. Hacer commit
git commit -m "feat: Configure SonarCloud integration"

# 5. Pushear la rama
git push origin feature/sonarcloud-config

# 6. Crear PR en GitHub
```

## Verificación

Después de crear el PR:
1. GitHub Actions correrá automáticamente el análisis
2. El resultado aparecerá en SonarCloud
3. Verifica en: https://sonarcloud.io/project/overview?id=PROJECT_KEY

## Requisitos Previos

- Git configurado en el repositorio
- PowerShell 5.0+
- Acceso a SonarCloud
- Token SONAR_TOKEN y SONAR_ORG configurados como secretos en GitHub

## Solución de Problemas

Si el script falla al actualizar `pom.xml`:
- Verifica que el archivo esté bien formado XML
- Actualiza manualmente el `pom.xml` con los valores de `sonar.projectKey` y `sonar.organization`

Si el workflow no corre:
- Verifica que los secretos `SONAR_TOKEN` y `SONAR_ORG` estén en GitHub
- Revisa los logs de GitHub Actions
