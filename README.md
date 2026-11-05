# SpringBootAPI_Tienda3v

API de Tienda 3V con Spring Boot, Spring Data JPA y MySQL. Incluye las entidades y repositorios de la tarea 12, y los servicios y controladores REST de la tarea 13.

## Ejecutar

Requisitos: Java 21 y MySQL 8.0.16 o posterior. Gradle se descarga mediante el wrapper incluido.

1. Crea la base de datos con [database/esquema.sql](database/esquema.sql), que corresponde al esquema de la tarea 11. En el cliente MySQL, desde la raíz del repositorio:

   ```sql
   source database/esquema.sql;
   ```

2. Configura la conexión en la terminal. Ejemplo en PowerShell:

   ```powershell
   $env:DB_URL = 'jdbc:mysql://localhost:3306/tienda3v'
   $env:DB_USERNAME = 'usuario_mysql'
   $env:DB_PASSWORD = 'contraseña_mysql'
   .\gradlew.bat bootRun
   ```

La API se publica en `http://localhost:8080`. `application.properties` utiliza esas variables y `ddl-auto=validate` para comprobar las entidades contra las tablas existentes. Ejecuta el SQL antes de arrancar; `create table if not exists` no migra una base que todavía tenga nombres de columnas anteriores.

## Endpoints

Todos los recursos tienen `GET` para listar y `POST` para crear en su ruta base, y `GET`, `PUT` y `DELETE` en `/{id}`.

| Recurso | Ruta base | Campo de ID en JSON |
| --- | --- | --- |
| Usuarios | `/api/usuarios` | `id` |
| Roles | `/api/roles` | `id` |
| Categorías | `/api/categorias` | `idCategoria` |
| Productos | `/api/productos` | `idProducto` |
| Sucursales | `/api/sucursales` | `id` |
| Cupones | `/api/cupones` | `idCupon` |
| Métodos de pago | `/api/metodos-pago` | `idMetodoPago` |
| Pedidos | `/api/pedidos` | `id` |
| Detalles de pedido | `/api/detalles-pedido` | `id` |
| Mensajes de contacto | `/api/mensajes-contacto` | `id` |
| Inventario por sucursal | `/api/inventario-sucursal` | `id.sucursalId`, `id.productoId` |

Inventario utiliza `/{idSucursal}/{idProducto}` en lugar de `/{id}`. Su POST recibe, por ejemplo:

```json
{"id":{"sucursalId":1,"productoId":1},"existencia":10}
```

Los IDs referenciados deben existir. Los demás POST omiten el ID porque MySQL lo genera. PUT recibe el cuerpo completo con los campos editables y conserva el identificador de la ruta. La creación devuelve `201`, la eliminación `204` y los registros inexistentes `404`.

También se conservan `GET /api/productos/activos`, `GET /api/productos/categoria/{idCategoria}` y `GET /api/categorias/activas`.

Ejemplo de producto para una categoría existente:

```json
{
  "categoria": {"idCategoria": 1},
  "nombreProducto": "Agua natural",
  "descripcionProducto": "Botella de un litro",
  "precio": 15.50,
  "activo": true
}
```

En usuarios, `rol` se envía como `{"id":1}` y `passwordHash` debe contener un hash ya generado; ese campo no se devuelve en las respuestas. Las fechas de cupones usan el formato `2026-11-01T12:00:00`. Los nombres SQL permanecen en minúsculas con guiones bajos; los atributos Java y JSON conservan la convención camelCase del proyecto.

## Pruebas

```powershell
.\gradlew.bat test
```

Las pruebas arrancan la API en un puerto aleatorio con una base H2 temporal y comprueban el CRUD de los 11 recursos, los errores 404, la clave compuesta del inventario y la serialización de usuarios y productos. No utilizan la base MySQL local.
