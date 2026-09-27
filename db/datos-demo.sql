-- ============================================================
-- Datos de demo para probar el sistema
-- Credenciales:
--   Gerente   1000000001 / admin123
--   Operador  2000000002 / oper123
--   Cliente   12345678   / cliente123
-- ============================================================

INSERT INTO tipos_vehiculo (id, nombre) VALUES
  ('11111111-1111-1111-1111-111111111111', 'Carro'),
  ('22222222-2222-2222-2222-222222222222', 'Moto')
ON CONFLICT (id) DO NOTHING;

INSERT INTO usuarios (id, nombre, cedula, rol, contrasena_hash) VALUES
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Gerente Demo',  '1000000001', 'GERENTE',
   'Sb9GEbb+5DJGjV92hlXoTw==:SIMlTzS0xqO1kq5R2nvddAIvOlTQ65DMdSRnjteFbGw='),
  ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Operador Demo', '2000000002', 'OPERADOR',
   'u+k0eph7joPpXwok29y9ag==:Ms8BkLFIGvKGt4q8o0faCqQ91BiBEe45fCOk209dPHA='),
  ('33333333-3333-3333-3333-333333333333', 'Juan Perez',    '12345678',   'CLIENTE',
   'lPvxnGCeOJHiI8XryRNXdg==:uOsOsItVcuMbv/MGyd6sfFkFzCq0QNbM0deECLLx1CY=')
ON CONFLICT (cedula) DO NOTHING;

INSERT INTO vehiculos (id, placa, owner_id, tipo_id) VALUES
  ('44444444-4444-4444-4444-444444444444', 'ABC-123',
   '33333333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111')
ON CONFLICT (placa) DO NOTHING;

INSERT INTO tarifa (id, id_tipo_vehiculo, valor_hora, anio_vigencia) VALUES
  ('55555555-5555-5555-5555-555555555555', '11111111-1111-1111-1111-111111111111', 1800, 2026),
  ('66666666-6666-6666-6666-666666666666', '22222222-2222-2222-2222-222222222222',  800, 2026)
ON CONFLICT (id) DO NOTHING;
