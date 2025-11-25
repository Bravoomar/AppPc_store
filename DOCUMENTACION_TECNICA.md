# Documentación Técnica - AppPC Store

## 📋 Índice

1. [Arquitectura](#arquitectura)
2. [Stack Tecnológico](#stack-tecnológico)
3. [Patrones de Diseño](#patrones-de-diseño)
4. [Estructura de Datos](#estructura-de-datos)
5. [Flujos de Datos](#flujos-de-datos)
6. [Configuración de Dependencias](#configuración-de-dependencias)
7. [Guía de Desarrollo](#guía-de-desarrollo)
8. [Testing](#testing)
9. [Despliegue](#despliegue)

## 🏗️ Arquitectura

### Arquitectura General

La aplicación sigue una **Arquitectura Limpia (Clean Architecture)** con separación en capas:

```
┌─────────────────────────────────────┐
│         UI Layer (Compose)          │
│  - Pantallas                         │
│  - Componentes                       │
│  - Navegación                        │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Presentation Layer              │
│  - ViewModels                        │
│  - State Management                  │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│        Domain Layer                  │
│  - Entidades                         │
│  - Casos de Uso                      │
│  - Repositorios (Interfaces)         │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         Data Layer                   │
│  - Repositorios (Implementaciones)   │
│  - API Services                      │
│  - Local Database (Room)              │
└──────────────────────────────────────┘
```

### Principios Aplicados

- **Separación de Responsabilidades**: Cada capa tiene una responsabilidad única
- **Inversión de Dependencias**: Las capas superiores dependen de abstracciones
- **Testabilidad**: Cada componente puede ser testeado de forma independiente
- **Escalabilidad**: Fácil agregar nuevas funcionalidades

## 🛠️ Stack Tecnológico

### Lenguaje
- **Kotlin 2.0.21**: Lenguaje principal de desarrollo

### UI Framework
- **Jetpack Compose**: Framework de UI declarativo
- **Material 3**: Sistema de diseño
- **Navigation Compose**: Navegación entre pantallas

### Arquitectura y Estado
- **ViewModel**: Gestión del ciclo de vida y estado
- **StateFlow**: Flujos reactivos de estado
- **Coroutines**: Programación asíncrona

### Inyección de Dependencias
- **Hilt**: Framework de DI basado en Dagger

### Persistencia
- **Room Database**: Base de datos local SQLite
- **DataStore** (preparado para futuras implementaciones)

### Networking
- **Retrofit**: Cliente HTTP para APIs REST
- **OkHttp**: Cliente HTTP subyacente
- **Gson**: Serialización JSON

### Testing
- **JUnit**: Framework de testing
- **Mockito**: Mocking para tests
- **Coroutines Test**: Testing de código asíncrono

## 🎨 Patrones de Diseño

### 1. Repository Pattern
```kotlin
interface RepositorioProductos {
    suspend fun obtenerTodosLosProductos(): Flow<List<Producto>>
    // ...
}

class RepositorioProductosImpl : RepositorioProductos {
    // Implementación que combina API y Room
}
```

**Ventajas**:
- Abstracción de la fuente de datos
- Fácil cambio entre API y datos locales
- Testeable con mocks

### 2. MVVM (Model-View-ViewModel)
```kotlin
@HiltViewModel
class ProductosViewModel @Inject constructor(
    private val repositorio: RepositorioProductos
) : ViewModel() {
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos.asStateFlow()
}
```

**Ventajas**:
- Separación clara entre UI y lógica
- Estado reactivo
- Ciclo de vida gestionado automáticamente

### 3. Dependency Injection (Hilt)
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit { ... }
}
```

**Ventajas**:
- Desacoplamiento de componentes
- Fácil testing
- Gestión automática del ciclo de vida

### 4. Observer Pattern (StateFlow)
```kotlin
val productos: StateFlow<List<Producto>> = _productos.asStateFlow()

// En la UI
val productos by viewModel.productos.collectAsState()
```

**Ventajas**:
- Actualizaciones reactivas de la UI
- Gestión automática de suscripciones

## 📊 Estructura de Datos

### Entidades Principales

#### Producto
```kotlin
data class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val categoria: String,
    val stock: Int,
    val imagenUrl: String? = null,
    val esActivo: Boolean = true,
    val tipoProducto: TipoProducto = TipoProducto.VENTA,
    val fechaCreacion: Long,
    val fechaActualizacion: Long
)
```

#### Cliente
```kotlin
data class Cliente(
    val id: String,
    val nombre: String,
    val email: String,
    val telefono: String?,
    val direccion: String?,
    val esActivo: Boolean = true,
    val rol: RolCliente = RolCliente.CLIENTE,
    val fechaCreacion: Long,
    val fechaActualizacion: Long
)
```

#### CarritoCompras
```kotlin
data class CarritoCompras(
    val items: List<ItemCarrito> = emptyList()
) {
    val montoTotal: Double
        get() = items.sumOf { it.precioTotal }
}
```

### Base de Datos Room

#### Esquema de Base de Datos
```sql
-- Tabla productos
CREATE TABLE productos (
    id TEXT PRIMARY KEY,
    nombre TEXT NOT NULL,
    descripcion TEXT NOT NULL,
    precio REAL NOT NULL,
    categoria TEXT NOT NULL,
    stock INTEGER NOT NULL,
    imagenUrl TEXT,
    esActivo INTEGER NOT NULL,
    tipoProducto TEXT NOT NULL,
    fechaCreacion INTEGER NOT NULL,
    fechaActualizacion INTEGER NOT NULL
);

-- Tabla clientes
CREATE TABLE clientes (
    id TEXT PRIMARY KEY,
    nombre TEXT NOT NULL,
    email TEXT NOT NULL,
    telefono TEXT,
    direccion TEXT,
    esActivo INTEGER NOT NULL,
    rol TEXT NOT NULL,
    fechaCreacion INTEGER NOT NULL,
    fechaActualizacion INTEGER NOT NULL
);

-- Tabla ventas
CREATE TABLE ventas (
    id TEXT PRIMARY KEY,
    clienteId TEXT NOT NULL,
    montoTotal REAL NOT NULL,
    estado TEXT NOT NULL,
    fechaVenta INTEGER NOT NULL,
    notas TEXT,
    itemsJson TEXT NOT NULL
);
```

## 🔄 Flujos de Datos

### Flujo de Carga de Productos

```
1. UI (PantallaPrincipal)
   ↓
2. ViewModel.cargarProductos()
   ↓
3. Repositorio.obtenerTodosLosProductos()
   ↓
4. Intenta API → Si falla → Room → Si vacío → Mock
   ↓
5. Actualiza StateFlow
   ↓
6. UI se actualiza automáticamente
```

### Flujo de Agregar al Carrito

```
1. Usuario hace clic en "Agregar al Carrito"
   ↓
2. ViewModel.agregarAlCarrito(producto, cantidad)
   ↓
3. Valida stock disponible
   ↓
4. Actualiza CarritoCompras
   ↓
5. Actualiza StateFlow del carrito
   ↓
6. UI muestra actualización
```

### Flujo de Sincronización

```
API Response
   ↓
Repositorio recibe datos
   ↓
Guarda en Room (caché local)
   ↓
Emite datos a ViewModel
   ↓
ViewModel actualiza StateFlow
   ↓
UI se actualiza
```

## ⚙️ Configuración de Dependencias

### Versiones Principales

```toml
[versions]
agp = "8.12.1"
kotlin = "2.0.21"
composeBom = "2024.09.00"
hilt = "2.48"
room = "2.6.1"
retrofit = "2.9.0"
```

### Módulos de Hilt

#### NetworkModule
- Proporciona: Retrofit, OkHttpClient, Gson
- Alcance: Singleton
- Configuración: Base URL, interceptores

#### DatabaseModule
- Proporciona: AppDatabase, DAOs
- Alcance: Singleton
- Configuración: Nombre de BD, migraciones

#### RepositoryModule
- Proporciona: Implementaciones de repositorios
- Alcance: Singleton
- Binding: Interfaces → Implementaciones

## 💻 Guía de Desarrollo

### Agregar una Nueva Pantalla

1. **Crear la pantalla en `ui/pantallas/`**
```kotlin
@Composable
fun NuevaPantalla(
    viewModel: NuevaViewModel,
    onNavigate: () -> Unit
) {
    // UI implementation
}
```

2. **Crear ViewModel si es necesario**
```kotlin
@HiltViewModel
class NuevaViewModel @Inject constructor(
    private val repositorio: Repositorio
) : ViewModel() {
    // State management
}
```

3. **Agregar ruta de navegación**
```kotlin
composable("nueva_pantalla") {
    NuevaPantalla(...)
}
```

### Agregar una Nueva Entidad

1. **Crear entidad de dominio**
```kotlin
data class NuevaEntidad(...)
```

2. **Crear entidad de Room**
```kotlin
@Entity(tableName = "nueva_entidad")
data class NuevaEntidadEntity(...)
```

3. **Crear DAO**
```kotlin
@Dao
interface NuevaEntidadDao {
    @Query("SELECT * FROM nueva_entidad")
    fun obtenerTodos(): Flow<List<NuevaEntidadEntity>>
}
```

4. **Actualizar AppDatabase**
```kotlin
abstract fun nuevaEntidadDao(): NuevaEntidadDao
```

### Agregar Validación de Formulario

1. **Agregar función de validación**
```kotlin
fun validarNuevoCampo(valor: String): String? {
    return when {
        valor.isBlank() -> "Campo requerido"
        // más validaciones
        else -> null
    }
}
```

2. **Crear componente de campo**
```kotlin
@Composable
fun CampoNuevo(
    valor: String,
    onValorChange: (String) -> Unit,
    error: String?
) {
    CampoTextoValidado(...)
}
```

## 🧪 Testing

### Estructura de Tests

```
app/src/test/
├── viewmodel/          # Tests de ViewModels
├── model/              # Tests de modelos
└── ui/                 # Tests de UI/validación
```

### Ejemplo de Test de ViewModel

```kotlin
class ProductosViewModelTest {
    @Mock
    private lateinit var repositorio: RepositorioProductos
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }
    
    @Test
    fun `test cargar productos`() = runTest {
        // Given
        val productos = listOf(...)
        whenever(repositorio.obtenerTodosLosProductos())
            .thenReturn(flowOf(productos))
        
        // When
        viewModel.cargarProductos()
        
        // Then
        assertEquals(productos, viewModel.productos.value)
    }
}
```

### Ejecutar Tests

```bash
# Todos los tests
./gradlew test

# Tests específicos
./gradlew test --tests "ProductosViewModelTest"

# Con cobertura
./gradlew test jacocoTestReport
```

## 🚀 Despliegue

### Build Variants

- **debug**: Para desarrollo
  - Application ID: `com.example.apppc_store.debug`
  - Usa debug keystore
  - Logging habilitado

- **release**: Para producción
  - Application ID: `com.example.apppc_store`
  - Usa release keystore
  - ProGuard deshabilitado (configurable)

### Proceso de Build

1. **Limpieza**
```bash
./gradlew clean
```

2. **Build de Release**
```bash
./gradlew assembleRelease
```

3. **Verificación**
```bash
./gradlew check
```

4. **APK Firmado**
El APK se genera en: `app/build/outputs/apk/release/app-release.apk`

### Configuración de CI/CD

Ejemplo para GitHub Actions:

```yaml
name: Build and Test

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '11'
      - name: Run tests
        run: ./gradlew test
      - name: Build APK
        run: ./gradlew assembleRelease
```

## 📝 Notas Adicionales

### Manejo de Errores

- Todos los repositorios manejan errores con try-catch
- Los ViewModels exponen estados de error
- La UI muestra mensajes de error amigables

### Optimizaciones

- LazyColumn para listas grandes
- Caché de imágenes (preparado para Coil)
- Paginación (preparado para Paging 3)

### Seguridad

- No se almacenan credenciales en código
- Validación de entrada en todos los formularios
- HTTPS para todas las comunicaciones

---

**Última actualización**: 2024
**Versión**: 1.0.0

