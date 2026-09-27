-- ============================================================
-- Datos de demo para probar el sistema
-- Credenciales (cumplen la politica: 8+, 1 mayuscula, 1 simbolo):
--   Gerente   1000000001 / Admin123!
--   Operador  2000000002 / Oper123!
--   Cliente   12345678   / Cliente123!
-- ============================================================

INSERT INTO tipos_vehiculo (id, nombre, formato_placa) VALUES
  ('11111111-1111-1111-1111-111111111111', 'Carro', 'CARRO'),
  ('22222222-2222-2222-2222-222222222222', 'Moto',  'MOTO')
ON CONFLICT (id) DO UPDATE SET formato_placa = EXCLUDED.formato_placa;

INSERT INTO usuarios (id, nombre, cedula, rol, contrasena_hash) VALUES
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Gerente Demo',  '1000000001', 'GERENTE',
   '+Ujm5cJcq5eT9n4QD87Nqg==:nk6nH+7RD1Y3bpH5trrhe0JCUo9caRjhsVnqVKv4SIo='),
  ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Operador Demo', '2000000002', 'OPERADOR',
   'vJhsm3yOJKbdboXHdaEqSw==:4nrMbL8/u+qxbATPZlHDRfppp/0U2lZTBLzMEB2xw3g='),
  ('33333333-3333-3333-3333-333333333333', 'Juan Perez',    '12345678',   'CLIENTE',
   'WXtqtqF9PGDMbiVRCCQXFw==:wcOXtTuMusIthbZSijEpb7bsEZHIB0FermxTFWmcAIQ=')
ON CONFLICT (cedula) DO UPDATE SET
  contrasena_hash = EXCLUDED.contrasena_hash,
  nombre = EXCLUDED.nombre,
  rol = EXCLUDED.rol;

INSERT INTO vehiculos (id, placa, owner_id, tipo_id) VALUES
  ('44444444-4444-4444-4444-444444444444', 'ABC-123',
   '33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111')
ON CONFLICT (placa) DO NOTHING;

INSERT INTO tarifa (id, id_tipo_vehiculo, valor_hora, anio_vigencia) VALUES
  ('55555555-5555-5555-5555-555555555555', '11111111-1111-1111-1111-111111111111', 1800, 2026),
  ('66666666-6666-6666-6666-666666666666', '22222222-2222-2222-2222-222222222222',  800, 2026)
ON CONFLICT (id) DO NOTHING;
