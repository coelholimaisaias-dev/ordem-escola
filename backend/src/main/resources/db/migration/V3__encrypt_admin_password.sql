-- V3 - Atualiza senha do usuário admin para senha criptografada

UPDATE usuario
SET senha = '$2a$10$cgRKmZzaLo9rGqufqJVbQ.tPwBzj8UBVfpBZUy.dEK148CLgCoUnq',
    atualizado_em = NOW(),
    atualizado_por = 'flyway'
WHERE email = 'admin@empresa.test';