# AppPC Store - Aplicación Android

## 📱 Descripción

AppPC Store es una aplicación Android moderna desarrollada con Jetpack Compose y Material 3, diseñada para gestionar una tienda de productos de tecnología. La aplicación incluye funcionalidades completas de gestión de productos, carrito de compras, clientes y ventas.

## ✨ Características Implementadas

### ✅ Diseño Visual Funcional con Material 3
- Interfaz moderna y atractiva usando Material Design 3
- Temas personalizables con esquemas de colores dinámicos
- Componentes reutilizables y consistentes
- Animaciones y transiciones fluidas

### ✅ Formularios Validados
- Validación en tiempo real de campos de formulario
- Mensajes de error descriptivos
- Validación de email, teléfono, precio, stock y más
- Componentes de formulario reutilizables

### ✅ Navegación Fluida
- Navegación basada en Navigation Compose
- Navegación entre pantallas con parámetros
- Deep linking support
- Gestión de estado de navegación

### ✅ Gestión de Estado
- ViewModels con StateFlow para gestión reactiva del estado
- Hilt para inyección de dependencias
- Estado compartido entre componentes
- Manejo de estados de carga y error

### ✅ Almacenamiento Local
- Room Database para persistencia local
- Entidades y DAOs para productos, clientes y ventas
- Sincronización automática con datos locales
- Caché inteligente de datos

### ✅ Consumo de APIs Externas
- Retrofit para comunicación con APIs REST
- Interceptores de logging para debugging
- Manejo de errores y fallbacks
- Soporte para APIs placeholder (JSONPlaceholder)

### ✅ Conexión con Microservicios Spring Boot
- Configuración lista para conectar con backend Spring Boot
- Servicios de API definidos para productos y clientes
- Estructura preparada para integración con microservicios
- Base URL configurable en NetworkModule

### ✅ Pruebas Unitarias
- Tests para ViewModels
- Tests para modelos de negocio (CarritoCompras)
- Tests para validación de formularios
- Cobertura de casos de uso principales

### ✅ Generación de APK Firmado
- Configuración de signing configs en build.gradle.kts
- Soporte para keystore personalizado
- Build types para debug y release
- Preparado para publicación en Google Play Store

## 🏗️ Arquitectura

La aplicación sigue una arquitectura limpia con las siguientes capas:

```
app/
├── data/
│   ├── local/          # Room Database, DAOs, Entities
│   └── remote/         # API Services, Retrofit
├── model/
│   ├── entidades/      # Modelos de dominio
│   └── repositorios/   # Interfaces y implementaciones
├── viewmodel/          # ViewModels con lógica de negocio
├── ui/
│   ├── pantallas/      # Pantallas principales
│   ├── componentes/    # Componentes reutilizables
│   ├── navegacion/     # Configuración de navegación
│   └── theme/          # Temas y estilos
└── di/                 # Módulos de Hilt para DI
```

## 📦 Dependencias Principales

- **Jetpack Compose**: UI moderna y declarativa
- **Material 3**: Componentes de diseño
- **Hilt**: Inyección de dependencias
- **Room**: Base de datos local
- **Retrofit**: Cliente HTTP para APIs
- **Navigation Compose**: Navegación entre pantallas
- **Coroutines & Flow**: Programación asíncrona
- **ViewModel**: Gestión del ciclo de vida y estado

## 🚀 Cómo Ejecutar

### Requisitos Previos
- Android Studio Hedgehog o superior
- JDK 11 o superior
- Android SDK 24 o superior
- Gradle 8.12.1

### Pasos para Ejecutar

1. **Clonar el repositorio**
   ```bash
   git clone <repository-url>
   cd AppPc_store
   ```

2. **Abrir en Android Studio**
   - Abre Android Studio
   - Selecciona "Open an Existing Project"
   - Navega a la carpeta del proyecto

3. **Sincronizar Gradle**
   - Android Studio sincronizará automáticamente
   - Espera a que termine la descarga de dependencias

4. **Ejecutar la aplicación**
   - Conecta un dispositivo Android o inicia un emulador
   - Haz clic en "Run" o presiona Shift+F10
   - La aplicación se instalará y ejecutará automáticamente

## 📝 Productos de Ejemplo

La aplicación incluye 4 productos de ejemplo predefinidos:

1. **Monitor 160Hz** - $450.00 (Stock: 8)
   - Monitor gaming de 27 pulgadas con frecuencia de actualización de 160Hz

2. **Kit Gamer** - $320.00 (Stock: 12)
   - Kit completo gamer: teclado mecánico RGB, mouse gaming, pad y auriculares

3. **Tarjeta Gráfica RTX 4060** - $550.00 (Stock: 5)
   - Tarjeta gráfica NVIDIA RTX 4060 con 8GB GDDR6

4. **Procesador AMD Ryzen 7 5800X** - $380.00 (Stock: 6)
   - Procesador AMD Ryzen 7 5800X de 8 núcleos y 16 hilos

## 🔧 Configuración de la API

### API Placeholder (Actual)
La aplicación está configurada para usar JSONPlaceholder como API placeholder:
- Base URL: `https://jsonplaceholder.typicode.com/`
- Los datos se cargan desde productos mock si la API falla

### Conectar con Spring Boot
Para conectar con tu backend Spring Boot:

1. **Actualizar NetworkModule.kt**
   ```kotlin
   private const val BASE_URL = "https://tu-backend-spring-boot.com/api/"
   ```

2. **Asegurar que los endpoints coincidan**
   - `/productos` - GET, POST, PUT, DELETE
   - `/clientes` - GET, POST, PUT, DELETE

3. **Configurar CORS en Spring Boot** (si es necesario)
   ```java
   @CrossOrigin(origins = "*")
   ```

## 🧪 Ejecutar Pruebas

### Pruebas Unitarias
```bash
./gradlew test
```

### Pruebas de Instrumentación
```bash
./gradlew connectedAndroidTest
```

## 📦 Generar APK Firmado

### 1. Crear un Keystore
```bash
keytool -genkey -v -keystore apppc-store.jks -keyalg RSA -keysize 2048 -validity 10000 -alias apppc-store
```

### 2. Configurar keystore.properties
Crea un archivo `keystore.properties` en la raíz del proyecto:
```properties
storePassword=tu-password
keyPassword=tu-password
keyAlias=apppc-store
storeFile=../apppc-store.jks
```

### 3. Actualizar build.gradle.kts
Actualiza el `signingConfigs` para leer desde `keystore.properties`:
```kotlin
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

signingConfigs {
    create("release") {
        storeFile = file(keystoreProperties["storeFile"] as String)
        storePassword = keystoreProperties["storePassword"] as String
        keyAlias = keystoreProperties["keyAlias"] as String
        keyPassword = keystoreProperties["keyPassword"] as String
    }
}
```

### 4. Generar APK
```bash
./gradlew assembleRelease
```

El APK se generará en: `app/build/outputs/apk/release/app-release.apk`

## 📚 Estructura del Proyecto

```
AppPc_store/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/apppc_store/
│   │   │   │   ├── data/          # Capa de datos
│   │   │   │   ├── di/             # Módulos de Hilt
│   │   │   │   ├── model/          # Modelos de dominio
│   │   │   │   ├── ui/             # Interfaz de usuario
│   │   │   │   └── viewmodel/      # ViewModels
│   │   │   └── res/                # Recursos
│   │   └── test/                   # Pruebas unitarias
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml         # Versiones de dependencias
├── build.gradle.kts
└── README.md
```

## 🎨 Pantallas Principales

1. **Pantalla Principal**: Lista de productos disponibles
2. **Pantalla Detalle Producto**: Detalles y agregar al carrito
3. **Pantalla Carrito**: Gestión del carrito de compras
4. **Pantalla Registro Cliente**: Formulario de registro
5. **Pantalla Agregar Producto**: Formulario para agregar productos

## 🔐 Permisos

La aplicación requiere los siguientes permisos:
- `INTERNET`: Para comunicación con APIs
- `ACCESS_NETWORK_STATE`: Para verificar conectividad

## 📄 Licencia

Este proyecto es de uso educativo y demostrativo.

## 👥 Contribuciones

Las contribuciones son bienvenidas. Por favor:
1. Fork el proyecto
2. Crea una rama para tu feature
3. Commit tus cambios
4. Push a la rama
5. Abre un Pull Request

## 📞 Soporte

Para preguntas o soporte, por favor abre un issue en el repositorio.

---

**Desarrollado con ❤️ usando Kotlin y Jetpack Compose**

