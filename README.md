# Gestión de Parqueadero — Taller Arquitectura por Capas

Prototipo funcional de un sistema de parqueadero (ingreso, permanencia, cálculo de tarifa y pago),
implementado en **Java puro con Servlets + JSP + JDBC y PostgreSQL, sin frameworks**.

## Arquitectura por capas

| Capa | Paquete | Contiene |
|------|---------|----------|
| Presentación | `web/WEB-INF/views` + `web/css` | JSP (vistas) y CSS |
| Controlador | `com.parqueamesta.controller` | Servlets (`XController`), `FiltroAutenticacion` |
| Servicio | `com.parqueamesta.services` | Reglas de negocio (`XService`) |
| Persistencia | `com.parqueamesta.persistence` | DAOs con JDBC (`RepositorioX`) |
| Modelo | `com.parqueamesta.model` | POJOs (Vehiculo, Usuario, Tarifa, RegistroIngreso, Pago...) |

Regla de oro: **el JSP nunca habla con la BD**; todo pasa por controlador → servicio → persistencia.

## Tecnologías

- **Java 22**
- **Jakarta Servlet 6.0 / JSP 3.1** (Apache Tomcat 10.1)
- **JSTL 3.0** (taglib `jakarta.tags.core`)
- **PostgreSQL 16** (driver `postgresql-42.7.4`)
- **Maven** (compila y empaqueta el WAR)

## Roles

| Rol | Puede hacer |
|-----|-------------|
| **Operador** | Registrar ingreso, ver "Vehículos dentro", dar salida, cobrar pagos, registrar clientes/vehículos |
| **Gerente** | Todo lo del operador + tarifas, tipos de vehículo, operadores y gerentes |
| **Cliente** | Registrar sus vehículos, ver sus tickets y **pagar online** |

## Puesta en marcha

### 1. Base de datos
```bash
# crear la base
createdb demo_parquadero
# (o) CREATE DATABASE demo_parquadero;

# crear las tablas
psql -d demo_parquadero -f db/schema.sql

# (opcional) datos de demo
psql -d demo_parquadero -f db/datos-demo.sql
```
La conexión está en `src/com/parqueamesta/persistence/utils/DB.java` (localhost:5432, `demo_parquadero`).

### 2. Compilar y empaquetar
```bash
mvn package
```
Genera `target/parqueadero-1.0.0.war`.

### 3. Desplegar en Tomcat
- Copia el WAR a `TOMCAT/webapps/ROOT.war` (para la raíz) y arranca Tomcat.
- Abre `http://localhost:8080/`.

## Credenciales demo (si cargaste `db/datos-demo.sql`)

| Rol | Cédula | Contraseña |
|-----|--------|-----------|
| Gerente | `1000000001` | `admin123` |
| Operador | `2000000002` | `oper123` |
| Cliente | `12345678` | `cliente123` |

> Si no cargas los datos demo, registra un **Gerente** en `/gerentes?accion=registrar`
> (el primero) y desde ahí crea operadores y clientes.

## Funcionalidades

- **Ingreso** por placa (el vehículo se crea si no existe), con validación según tipo:
  - Carro `ABC-123` (LLL-###) · Moto `ABC-12A` (LLL-##L)
- **Vehículos dentro**: panel con los vehículos adentro y salida en un clic
- **Salida**: calcula el valor (mínimo 1 hora, fracciones hacia arriba) y genera el pago
- **Pago**: por el **cliente online** o por el **operador en caja**
- **Tarifas** por tipo de vehículo y año (modificables); tipos de vehículo nuevos
- **CRUD completo** (crear, listar, editar, eliminar) de clientes, operadores, gerentes, vehículos y tipos
- **Login** con contraseña hasheada (**PBKDF2-HMAC-SHA256**) y redirección según rol

## Tests
```bash
mvn test
```
22 pruebas de la capa de servicio y persistencia (requieren PostgreSQL arriba).

## Documentación de diseño
- `docs/domain/UC0-diagram.puml` — casos de uso (nivel 0)
- `docs/domain/classes-diagram.puml` — modelo de clases
- `docs/domain/diagrama-paquetes.puml` — arquitectura por capas (paquetes)
