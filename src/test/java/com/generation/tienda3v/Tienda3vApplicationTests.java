package com.generation.tienda3v;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.datasource.url=jdbc:h2:mem:tienda3v_test;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
class Tienda3vApplicationTests {

    @LocalServerPort
    private int puerto;

    private final HttpClient cliente = HttpClient.newHttpClient();
    private final JsonMapper json = JsonMapper.builder().build();

    @Test
    void crudRoles() throws Exception {
        String datos = "{\"nombre\":\"rol-" + unico() + "\"}";
        String cambios = "{\"nombre\":\"rol-" + unico() + "\"}";
        probarCrud("roles", "id", datos, cambios, "nombre");
    }

    @Test
    void crudUsuariosNoExponePasswordHash() throws Exception {
        String datos = datosUsuario(crearRol());
        probarCrud("usuarios", "id", datos, datos.replace("Ana", "Luisa"), "nombre");
    }

    @Test
    void crudSucursales() throws Exception {
        String datos = datosSucursal();
        probarCrud("sucursales", "id", datos,
                datos.replace("Sucursal prueba", "Sucursal actualizada"), "nombre");
    }

    @Test
    void crudCategorias() throws Exception {
        String datos = datosCategoria();
        probarCrud("categorias", "idCategoria", datos,
                datos.replace("Categoria prueba", "Categoria actualizada"), "nombre");
    }

    @Test
    void crudProductosSinRecursionConCategoria() throws Exception {
        long idCategoria = crear("categorias", datosCategoria()).get("idCategoria").asLong();
        String datos = datosProducto(idCategoria);
        long idProductoRelacionado = crear("productos", datos).get("idProducto").asLong();
        probarCrud("productos", "idProducto", datos,
                datos.replace("Producto prueba", "Producto actualizado"), "nombreProducto");
        HttpResponse<String> respuesta = enviar("GET", "/api/categorias/" + idCategoria, null);
        assertEquals(200, respuesta.statusCode(), respuesta.body());
        assertEquals(idCategoria, json.readTree(respuesta.body()).get("idCategoria").asLong());
        assertEquals(204, enviar("DELETE", "/api/productos/" + idProductoRelacionado, null).statusCode());
    }

    @Test
    void crudCupones() throws Exception {
        String datos = """
                {"codigo":"CUPON-%s","porcentajeDescuento":10,
                 "fechaInicio":"2026-01-01T00:00:00","fechaFin":"2026-12-31T23:59:59",
                 "activo":true,"maximoUsos":10,"usos":0}
                """.formatted(unico());
        probarCrud("cupones", "idCupon", datos,
                datos.replace("\"porcentajeDescuento\":10", "\"porcentajeDescuento\":15"),
                "porcentajeDescuento");
    }

    @Test
    void crudMetodosPago() throws Exception {
        String datos = "{\"nombre\":\"Tarjeta-" + unico() + "\",\"activo\":true}";
        probarCrud("metodos-pago", "idMetodoPago", datos,
                datos.replace("Tarjeta-", "Efectivo-"), "nombre");
    }

    @Test
    void crudPedidos() throws Exception {
        String datos = crearDatosPedido();
        probarCrud("pedidos", "id", datos,
                datos.replace("\"estado\":\"pendiente\"", "\"estado\":\"entregado\""), "estado");
    }

    @Test
    void crudDetallesPedido() throws Exception {
        long idPedido = crear("pedidos", crearDatosPedido()).get("id").asLong();
        long idCategoria = crear("categorias", datosCategoria()).get("idCategoria").asLong();
        long idProducto = crear("productos", datosProducto(idCategoria)).get("idProducto").asLong();
        String datos = """
                {"pedidoId":%d,"productoId":%d,"nombreProducto":"Producto prueba",
                 "cantidad":2,"precioUnitario":25.50,"subtotal":51.00}
                """.formatted(idPedido, idProducto);
        String cambios = datos.replace("\"cantidad\":2", "\"cantidad\":3")
                .replace("\"subtotal\":51.00", "\"subtotal\":76.50");
        probarCrud("detalles-pedido", "id", datos, cambios, "cantidad");
    }

    @Test
    void crudMensajesContacto() throws Exception {
        long idUsuario = crear("usuarios", datosUsuario(crearRol())).get("id").asLong();
        String datos = """
                {"usuarioId":%d,"nombre":"Ana","correo":"ana@example.com",
                 "telefono":"5512345678","motivo":"Informacion","mensaje":"Consulta de prueba",
                 "aceptaAvisoPrivacidad":true,"estado":"nuevo"}
                """.formatted(idUsuario);
        probarCrud("mensajes-contacto", "id", datos,
                datos.replace("Consulta de prueba", "Consulta actualizada"), "mensaje");
    }

    @Test
    void crudInventarioConClaveCompuesta() throws Exception {
        long idSucursal = crear("sucursales", datosSucursal()).get("id").asLong();
        long idCategoria = crear("categorias", datosCategoria()).get("idCategoria").asLong();
        long idProducto = crear("productos", datosProducto(idCategoria)).get("idProducto").asLong();
        String datos = """
                {"id":{"sucursalId":%d,"productoId":%d},"sucursal":{"id":%d},
                 "producto":{"idProducto":%d},"existencia":10}
                """.formatted(idSucursal, idProducto, idSucursal, idProducto);
        JsonNode creado = crear("inventario-sucursal", datos);
        assertEquals(idSucursal, creado.get("id").get("sucursalId").asLong());
        assertEquals(idProducto, creado.get("id").get("productoId").asLong());

        String ruta = "/api/inventario-sucursal/" + idSucursal + "/" + idProducto;
        assertEquals(200, enviar("GET", "/api/inventario-sucursal", null).statusCode());
        assertEquals(200, enviar("GET", ruta, null).statusCode());
        HttpResponse<String> actualizado = enviar("PUT", ruta,
                datos.replace("\"existencia\":10", "\"existencia\":15"));
        assertEquals(200, actualizado.statusCode(), actualizado.body());
        assertEquals(15, json.readTree(enviar("GET", ruta, null).body()).get("existencia").asLong());

        String rutaAusente = "/api/inventario-sucursal/999999/999999";
        int cantidadAntes = json.readTree(enviar("GET", "/api/inventario-sucursal", null).body()).size();
        assertEquals(404, enviar("PUT", rutaAusente, datos).statusCode());
        assertEquals(cantidadAntes, json.readTree(enviar("GET", "/api/inventario-sucursal", null).body()).size());
        assertEquals(404, enviar("GET", rutaAusente, null).statusCode());
        assertEquals(404, enviar("DELETE", rutaAusente, null).statusCode());
        assertEquals(204, enviar("DELETE", ruta, null).statusCode());
        assertEquals(404, enviar("GET", ruta, null).statusCode());
    }

    private void probarCrud(String recurso, String campoId, String datos, String cambios,
                            String campoActualizado) throws Exception {
        String base = "/api/" + recurso;
        JsonNode creado = crear(recurso, datos);
        long id = creado.get(campoId).asLong();
        String ruta = base + "/" + id;
        HttpResponse<String> consulta = enviar("GET", ruta, null);
        assertEquals(200, consulta.statusCode(), consulta.body());
        assertEquals(id, json.readTree(consulta.body()).get(campoId).asLong());
        HttpResponse<String> lista = enviar("GET", base, null);
        assertEquals(200, lista.statusCode(), lista.body());
        boolean encontrado = false;
        for (JsonNode registro : json.readTree(lista.body())) {
            encontrado |= registro.get(campoId).asLong() == id;
            if (recurso.equals("usuarios")) {
                assertFalse(registro.has("passwordHash"));
            }
        }
        assertTrue(encontrado);

        HttpResponse<String> actualizado = enviar("PUT", ruta, cambios);
        assertEquals(200, actualizado.statusCode(), actualizado.body());
        JsonNode guardado = json.readTree(enviar("GET", ruta, null).body());
        JsonNode esperado = json.readTree(cambios).get(campoActualizado);
        if (esperado.isNumber()) {
            assertEquals(esperado.asDouble(), guardado.get(campoActualizado).asDouble());
        } else {
            assertTrue(esperado.asString().equalsIgnoreCase(guardado.get(campoActualizado).asString()));
        }
        if (recurso.equals("usuarios")) {
            assertFalse(creado.has("passwordHash"));
            assertFalse(guardado.has("passwordHash"));
            assertFalse(lista.body().contains("hash-solo-prueba"));
        }

        int cantidadAntes = json.readTree(enviar("GET", base, null).body()).size();
        assertEquals(404, enviar("PUT", base + "/999999", datos).statusCode());
        assertEquals(cantidadAntes, json.readTree(enviar("GET", base, null).body()).size());
        assertEquals(404, enviar("GET", base + "/999999", null).statusCode());
        assertEquals(404, enviar("DELETE", base + "/999999", null).statusCode());
        assertEquals(204, enviar("DELETE", ruta, null).statusCode());
        assertEquals(404, enviar("GET", ruta, null).statusCode());
    }

    private JsonNode crear(String recurso, String datos) throws Exception {
        HttpResponse<String> respuesta = enviar("POST", "/api/" + recurso, datos);
        assertEquals(201, respuesta.statusCode(), recurso + ": " + respuesta.body());
        return json.readTree(respuesta.body());
    }

    private HttpResponse<String> enviar(String metodo, String ruta, String datos) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + puerto + ruta))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json")
                .method(metodo, datos == null ? HttpRequest.BodyPublishers.noBody()
                        : HttpRequest.BodyPublishers.ofString(datos))
                .build();
        return cliente.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private long crearRol() throws Exception {
        return crear("roles", "{\"nombre\":\"rol-" + unico() + "\"}").get("id").asLong();
    }

    private String datosUsuario(long idRol) {
        return """
                {"rol":{"id":%d},"nombre":"Ana","apellido":"Perez",
                 "correo":"usuario-%s@example.com","telefono":"5512345678",
                 "passwordHash":"hash-solo-prueba","aceptaTerminos":true,"activo":true}
                """.formatted(idRol, unico());
    }

    private String datosSucursal() {
        return """
                {"nombre":"Sucursal prueba","direccion":"Calle %s","ciudad":"Mexico",
                 "estado":"CDMX","codigoPostal":"01000","activa":true}
                """.formatted(unico());
    }

    private String datosCategoria() {
        String identificador = unico();
        return "{\"nombre\":\"Categoria prueba " + identificador
                + "\",\"slug\":\"categoria-" + identificador + "\",\"activa\":true}";
    }

    private String datosProducto(long idCategoria) {
        return """
                {"categoria":{"idCategoria":%d},"nombreProducto":"Producto prueba",
                 "descripcionProducto":"Producto de prueba","precio":25.50,"activo":true}
                """.formatted(idCategoria);
    }

    private String crearDatosPedido() throws Exception {
        long idUsuario = crear("usuarios", datosUsuario(crearRol())).get("id").asLong();
        long idSucursal = crear("sucursales", datosSucursal()).get("id").asLong();
        long idMetodo = crear("metodos-pago", "{\"nombre\":\"Pago-" + unico() + "\",\"activo\":true}")
                .get("idMetodoPago").asLong();
        return """
                {"usuarioId":%d,"sucursalId":%d,"metodoPagoId":%d,"estado":"pendiente",
                 "estadoPago":"pendiente","nombreCliente":"Ana Perez",
                 "correoCliente":"ana@example.com","telefonoCliente":"5512345678",
                 "subtotal":51.00,"descuento":0.00,"total":51.00}
                """.formatted(idUsuario, idSucursal, idMetodo);
    }

    private String unico() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
