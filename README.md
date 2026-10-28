# MPAZ Ticket

Aplicación móvil Android para registrar y hacer seguimiento a las solicitudes de mantención de la **Escuela Escritora Marcela Paz** (San Bernardo). Reemplaza el aviso verbal de problemas de infraestructura, mobiliario y tecnología por un registro centralizado con número de ticket, estado, prioridad y responsable.

> Proyecto académico — DSY1105 Desarrollo de Aplicaciones Móviles, Duoc UC.
> Todos los datos de prueba (usuarios, tickets, fotos) son ficticios.

## Integrantes

| Nombre | Rol en el desarrollo |
|---|---|
| Steven peralta | Base de datos Room, ViewModels, login/registro, cámara y galería |
| Javier Calquin | Validaciones de formularios, componentes reutilizables, animaciones, tema Material 3 |

## Funcionalidades implementadas

**Usuarios y roles**
- Registro e inicio de sesión con tres perfiles: **Usuario** (solicitante), **Responsable** (TI / auxiliar) y **Admin** (encargado de TI).
- Cada rol ve una pantalla distinta: el solicitante ve sus tickets, el responsable solo los asignados y el admin todos.

**Tickets**
- Creación de ticket con título, descripción, categoría, ubicación, prioridad y fotografía opcional.
- Número único de ticket (`#id`) y estado visible con etiqueta de color.
- El admin asigna el ticket a un responsable; el responsable actualiza el estado (Pendiente, Revisado, Asignado, En Proceso, Solucionado, Cerrado).
- Ordenamiento de la lista por prioridad (admin y responsable).
- Vista de detalle con foto ampliable.

**Formularios validados**
- Validación por campo con borde rojo, ícono de error/correcto y mensaje animado.
- Lógica de validación separada de la interfaz (`validacion/Validaciones.kt`).

**Recursos nativos**
- **Cámara**: captura de foto del problema, con solicitud de permiso en tiempo de ejecución.
- **Galería**: selección de una imagen existente.

**Persistencia local**
- Base de datos **Room (SQLite)** con las tablas `usuarios` y `tickets`; los datos se mantienen al cerrar la app.

**Animaciones**
- Mensajes de error que aparecen/desaparecen (`AnimatedVisibility`).
- Cambio de color animado del estado (`animateColorAsState`).
- Reordenamiento animado de la lista (`animateItem`).
- Transiciones entre pantallas (`ActivityOptions.makeCustomAnimation`).
- Vista previa animada de la foto con bordes redondeados.

## Arquitectura (MVVM)

```
app/src/main/java/com/example/mpaztickets/
├── data/                 Model + acceso a datos
│   ├── Ticket.kt, Usuario.kt          Entidades Room (@Entity)
│   ├── TicketDAO.kt, UsuarioDAO.kt    Consultas SQL (@Dao)
│   ├── TicketDatabase.kt              Base de datos Room (singleton)
│   ├── TicketRepository.kt, UsuarioRepository.kt   Intermediarios entre DAO y ViewModel
│   └── Catalogos.kt                   Listas de categorías, ubicaciones y prioridades
├── validacion/
│   └── Validaciones.kt                Reglas de validación (sin dependencia de la UI)
├── ui/
│   ├── TicketViewModel.kt, UsuarioViewModel.kt   Estado y lógica (StateFlow / Flow)
│   ├── *Screen.kt                     Pantallas Composable
│   ├── componentes/                   Composables reutilizables (CampoTexto, CampoSelector, EtiquetaEstado)
│   └── theme/                         Tema Material 3
└── *Activity.kt                       Una Activity por pantalla, navegación con Intent
```

Flujo: **Screen → ViewModel → Repository → DAO → Room**. La interfaz observa los `Flow` de Room con `collectAsState()`, por lo que se actualiza sola cuando cambian los datos.

## Tecnologías

- Kotlin + Jetpack Compose + Material 3
- Room (SQLite) con KSP
- ViewModel, StateFlow, Coroutines
- Coil (carga de imágenes)
- Activity Result API (cámara, galería y permisos)

## Pasos para ejecutar

1. Clonar el repositorio:
   ```
   git clone https://github.com/peraltastevenryo/mpaztickets.git
   ```
2. Abrir la carpeta en **Android Studio** (File → Open) y esperar el *Gradle Sync*.
3. Crear o seleccionar un emulador con Android 7.0 (API 24) o superior, o conectar un teléfono con depuración USB.
4. Presionar **Run ▶**.
5. En la app, crear al menos una cuenta de cada rol desde "Crea una aqui" para probar el flujo completo:
   - Un **Usuario** crea un ticket.
   - El **Admin** lo asigna a un responsable.
   - El **Responsable** cambia su estado.

## Planificación

Tablero Trello: 
