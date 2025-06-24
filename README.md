# 🧩 8-Puzzle Game (JavaFX + Maven)

Este es un proyecto final de programación desarrollado en Java utilizando JavaFX para la interfaz gráfica y Maven para la gestión del proyecto. El juego permite jugar al clásico **8-Puzzle** en dos modos: **manual** e **inteligente (IA)**, implementando técnicas como **Ramificación y Poda (Branch and Bound)**.

---

## 🚀 Características

- ✅ Movimiento interactivo de fichas (modo manual).
- ✅ Generación aleatoria de tableros resolubles.
- ✅ Sugerencia de movimientos.
- ✅ Resolución automática paso a paso (modo IA).
- ✅ Sistema de usuarios con registro e inicio de sesión.
- ✅ Almacenamiento y visualización de puntuaciones.
- ❌ Música (pendiente por compatibilidad con JavaFX Media).
- ❌ Niveles múltiples (actualmente un solo nivel).

---

## 📁 Estructura del proyecto

- `/src/main/java`: Código fuente del juego (dividido por pantallas, lógica y modelos).
- `/src/main/resources`: Recursos estáticos (fxml, imágenes, etc. si se usan).
- `pom.xml`: Archivo de configuración Maven con las dependencias necesarias.

---

## 🛠️ Requisitos Previos

Antes de compilar o ejecutar el proyecto, asegúrate de contar con lo siguiente:

### 1. Java JDK

- Recomendado: **Java 17**
- Puedes verificar tu versión con:
  ```bash
  java -version
  ```
  
**2. Maven**

Instala Apache Maven si no lo tienes.

- Verifica con:
```bash
  mvn -v
```

**3. IntelliJ IDEA**

Se recomienda IntelliJ IDEA Community Edition o Ultimate.

Asegúrate de tener instalado el JavaFX plugin (puedes habilitarlo en "Settings → Plugins").

**📦 Configuración en IntelliJ IDEA**

**1. Clona el repositorio:**

```bash
git clone https://github.com/tuusuario/8puzzle-javafx.git
cd 8puzzle-javafx
```
**2. Abre IntelliJ IDEA y selecciona Open para abrir la carpeta del proyecto.**

**3. IntelliJ detectará automáticamente el archivo pom.xml y configurará el proyecto como un proyecto Maven.**

**4. Configura JavaFX SDK en IntelliJ:**

- Ve a File → Project Structure → Libraries → + → Java.

- Selecciona la carpeta del SDK de JavaFX que hayas descargado.

- Aplica y guarda.

**5. Agrega las VM Options para JavaFX en tu configuración de ejecución:**

- Ve a Run → Edit Configurations.

- En el campo VM options, añade lo siguiente (ajusta la ruta a tu JavaFX):

```bash
--module-path "/ruta/a/javafx-sdk-XX/lib" --add-modules javafx.controls,javafx.fxml
```
Ejemplo en Windows:

```cpp
--module-path "C:\javafx-sdk-17.0.2\lib" --add-modules javafx.controls,javafx.fxml
```
**6. Compila y ejecuta el proyecto:**

- Usa Build → Build Project.
  
- Luego Run → Run 'Main' o el archivo principal correspondiente (por ejemplo, MainMenuScreen, PuzzleApp, etc.).

**📥 Dependencias principales**
Estas ya están configuradas en pom.xml:

```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>17.0.2</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>17.0.2</version>
    </dependency>
</dependencies>
```
Si quieres usar más módulos como javafx.media, puedes agregarlos al pom.xml y a los VM options.

**👥 Créditos**

Desarrollado por:

Andrés Joseph Pacheco Carlos

Brayan Alejandro Pamila Torres

**📄 Licencia**

Este proyecto está bajo la licencia MIT.
