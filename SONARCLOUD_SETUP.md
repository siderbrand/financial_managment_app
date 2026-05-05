# Configuración de SonarCloud

Esta guía te ayudará a configurar completamente SonarCloud en tu proyecto.

## Pasos de Configuración

### 1. Crear Cuenta en SonarCloud

1. Ve a [https://sonarcloud.io](https://sonarcloud.io)
2. Haz clic en "Log in with GitHub" (o tu proveedor de Git preferido)
3. Autoriza SonarCloud para acceder a tu cuenta de GitHub

### 2. Crear una Organización (si no la tienes)

1. En SonarCloud, ve a "My Organizations"
2. Haz clic en "Create organization"
3. Sigue los pasos y anota el nombre de tu organización

### 3. Crear un Proyecto

1. Después de crear la organización, haz clic en "Create project"
2. Selecciona tu repositorio de GitHub
3. Sigue los pasos de configuración
4. Anota el **Project Key** (`fabrica-escuela`)
5. Anota también la **Organization Key** exacta de SonarCloud, no el nombre visible de la organización

### 4. Generar Token de Autenticación

1. Ve a tu perfil en SonarCloud
2. Haz clic en "Security"
3. Genera un nuevo token (copia el token completo)

### 5. Configurar Secretos en GitHub

1. Ve a tu repositorio en GitHub
2. Settings → Secrets and variables → Actions
3. Haz clic en "New repository secret" y agrega:

```
Nombre: SONAR_TOKEN
Valor: (pega el token que generaste)

Nombre: SONAR_ORG
Valor: (tu nombre de organización en SonarCloud)
```

### 6. Actualizar `sonar-project.properties`

La organización debe coincidir con la organization key de SonarCloud:

```properties
sonar.organization=fabrica-escuela
sonar.projectKey=fabrica-escuela
```

### 7. Actualizar `pom.xml`

Confirma los mismos valores en `pom.xml`:

```xml
<sonar.organization>fabrica-escuela</sonar.organization>
<sonar.projectKey>fabrica-escuela</sonar.projectKey>
```

## Ejecutar Análisis Localmente

Para ejecutar el análisis de SonarCloud localmente:

```bash
mvn clean verify sonar:sonar \
    -Dsonar.projectKey=fabrica-escuela \
    -Dsonar.organization=fabrica-escuela \
  -Dsonar.host.url=https://sonarcloud.io \
    -Dsonar.token=tu-token-aqui
```

O si prefieres usar variables de entorno:

```bash
export SONAR_TOKEN=tu-token-aqui

mvn clean verify sonar:sonar \
    -Dsonar.projectKey=fabrica-escuela \
    -Dsonar.organization=fabrica-escuela
```

## Ejecutar Análisis Automáticamente

El workflow de GitHub Actions (`.github/workflows/sonarcloud.yml`) se ejecutará automáticamente:
- En cada push a las ramas `main` y `develop`
- En cada pull request a esas ramas

## Ver Resultados

1. Ve a [https://sonarcloud.io](https://sonarcloud.io)
2. Accede a tu organización y proyecto
3. Verás un dashboard con:
   - Quality Gate (Puerta de Calidad)
   - Coverage (Cobertura de Pruebas)
   - Code Smells
   - Bugs
   - Vulnerabilities
   - Duplicated Code

## Requisitos Recomendados

### JaCoCo para Cobertura de Código

Agrega esto a tu `pom.xml` para obtener reportes de cobertura:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Solución de Problemas

### El workflow falla con "SONAR_TOKEN not provided"
- Verifica que agregaste el secreto `SONAR_TOKEN` en GitHub

### El proyecto no aparece en SonarCloud
- Verifica que el `sonar.projectKey` coincida en todos los archivos
- Verifica que el `sonar.organization` sea correcto

### Quality Gate falla
- Revisa los requisitos del Quality Gate en SonarCloud
- Aumenta la cobertura de pruebas
- Arregla los bugs y vulnerabilidades reportados

## Documentación Oficial

- [SonarCloud](https://sonarcloud.io)
- [SonarQube Maven Plugin](https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-maven/)
- [GitHub Actions para SonarCloud](https://github.com/SonarSource/sonarcloud-github-action)
