-- ============================================================
-- Schema del parqueadero (base de datos: demo_parquadero)
-- Una sola tabla de personas: usuarios (con columna rol)
-- ============================================================

-- Personas del sistema (clientes, operadores y gerentes)
-- Se distinguen por la columna "rol" (CLIENTE | OPERADOR | GERENTE)
CREATE TABLE IF NOT EXISTS usuarios (
    id              uuid         PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre          varchar(100) NOT NULL,
    cedula          varchar(20)  NOT NULL UNIQUE,
    rol             varchar(20)  NOT NULL,
    contrasena_hash varchar(255)
);

-- Tipos de vehículo (Carro, Moto, Camioneta, ...)
CREATE TABLE IF NOT EXISTS tipos_vehiculo (
    id          uuid         PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre      varchar(50)  NOT NULL,
    descripcion varchar(200)
);

-- Vehículos; el propietario es un usuario con rol CLIENTE
CREATE TABLE IF NOT EXISTS vehiculos (
    id       uuid        PRIMARY KEY DEFAULT gen_random_uuid(),
    placa    varchar(20) NOT NULL UNIQUE,
    owner_id uuid        REFERENCES usuarios(id),
    tipo_id  uuid        REFERENCES tipos_vehiculo(id)
);

-- Tarifa por hora de cada tipo de vehículo, por año de vigencia
CREATE TABLE IF NOT EXISTS tarifa (
    id               uuid          PRIMARY KEY DEFAULT gen_random_uuid(),
    id_tipo_vehiculo uuid          REFERENCES tipos_vehiculo(id),
    valor_hora       numeric(10,2) NOT NULL,
    anio_vigencia    integer       NOT NULL
);

-- Registro de entrada/salida de un vehículo (el "ticket")
CREATE TABLE IF NOT EXISTS registro_ingreso (
    id                  uuid      PRIMARY KEY DEFAULT gen_random_uuid(),
    id_vehiculo         uuid      REFERENCES vehiculos(id),
    hora_entrada        timestamp,
    hora_salida         timestamp,
    id_operador_entrada uuid,
    id_operador_salida  uuid
);

-- Pago generado al cerrar un ticket
CREATE TABLE IF NOT EXISTS pago (
    id                  uuid          PRIMARY KEY DEFAULT gen_random_uuid(),
    id_registro_ingreso uuid          REFERENCES registro_ingreso(id),
    valor               numeric(10,2) NOT NULL,
    fecha_pago          timestamp
);
