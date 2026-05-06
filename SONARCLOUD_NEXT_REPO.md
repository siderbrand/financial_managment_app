# Guía: Configurar SonarCloud en otro repositorio

Plantilla breve y lista para pegar en otro repo dentro de la misma organización.

## Lo mínimo que necesitas
- `sonar-project.properties`
- `.github/workflows/sonarcloud.yml`
- `pom.xml` si el repo es Maven

## Valores que debes cambiar
- `TU_PROJECT_KEY`: la clave del proyecto en SonarCloud para ese repo.
- `fabrica-escuela`: la organization key de SonarCloud.

## `sonar-project.properties`

```properties
sonar.projectKey=TU_PROJECT_KEY
sonar.organization=fabrica-escuela
sonar.host.url=https://sonarcloud.io
sonar.sources=src/main/java,src/main/resources
sonar.tests=src/test/java
sonar.sourceEncoding=UTF-8
sonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
sonar.qualitygate.wait=true
```

## `.github/workflows/sonarcloud.yml`

```yaml
name: SonarCloud Analysis

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
        with:
          fetch-depth: 0

      - uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'
          cache: maven

      - uses: actions/cache@v4
        with:
          path: ~/.sonar/cache
          key: ${{ runner.os }}-sonar
          restore-keys: ${{ runner.os }}-sonar

      - uses: actions/cache@v4
        with:
          path: ~/.m2
          key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
          restore-keys: ${{ runner.os }}-m2

      - name: Make Maven wrapper executable
        run: chmod +x mvnw

      - name: Build and analyze
        env:
          GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
        run: ./mvnw -B verify sonar:sonar -Dsonar.projectKey=TU_PROJECT_KEY -Dsonar.organization=fabrica-escuela

      - name: Upload test results
        if: failure()
        uses: actions/upload-artifact@v4
        with:
          name: test-results
          path: target/surefire-reports/
```

## `pom.xml` si el repo es Maven

```xml
<properties>
  <sonar.projectKey>TU_PROJECT_KEY</sonar.projectKey>
  <sonar.organization>fabrica-escuela</sonar.organization>
  <sonar.host.url>https://sonarcloud.io</sonar.host.url>
  <maven.compiler.release>21</maven.compiler.release>
</properties>
```

Si quieres cobertura, agrega JaCoCo:

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

## Secrets en GitHub
- `SONAR_TOKEN`: token de SonarCloud
- `SONAR_ORG`: `fabrica-escuela`

## Flujo recomendado

```powershell
cd C:\ruta\al\repo-destino
git checkout -b feature/sonarcloud-config
git add sonar-project.properties .github/workflows/sonarcloud.yml pom.xml
git commit -m "ci: add SonarCloud configuration"
git push origin feature/sonarcloud-config
```

## Verificación local

```powershell
$env:SONAR_TOKEN = "TU_TOKEN"
./mvnw clean verify sonar:sonar -Dsonar.projectKey=TU_PROJECT_KEY -Dsonar.organization=fabrica-escuela -Dsonar.host.url=https://sonarcloud.io
```

## Notas
- Usa siempre la organization key, no el nombre visible.
- Mantén el PR inicial pequeño: solo los archivos funcionales.
- Si necesitas automatizar esto en varios repos, usa un script sin rutas absolutas.