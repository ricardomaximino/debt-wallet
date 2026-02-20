DELETE FROM payment;
DELETE FROM items;
DELETE FROM debt;
DELETE FROM wallet;
DELETE FROM user_workspace;
DELETE FROM workspace;
DELETE FROM user_roles;
DELETE FROM users;

-- Workspaces
INSERT INTO workspace (id, name, slug) VALUES 
('11111111-1111-1111-1111-111111111111', 'Maximino & Associados', 'maximino-associados'),
('22222222-2222-2222-2222-222222222222', 'Global Law Partners', 'global-law');

-- Users (Lawyer)
INSERT INTO users (id, name, email, username, password, enabled, plan_role) 
VALUES ('fd4437c0-bda6-489d-964f-7e43169cace0', 'Ricardo Maximino', 'ricardo@email.com', 'admin', '{noop}password', true, 'PROFESSIONAL');

INSERT INTO user_roles (user_id, role) VALUES ('fd4437c0-bda6-489d-964f-7e43169cace0', 'ADMIN');
INSERT INTO user_workspace (user_id, workspace_id) VALUES ('fd4437c0-bda6-489d-964f-7e43169cace0', '11111111-1111-1111-1111-111111111111');

-- Users (Clients)
-- 1. Incorporadora Alfa S.A. (Corporativo)
INSERT INTO users (id, name, email, username, password, enabled, plan_role) 
VALUES ('c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f', 'Incorporadora Alfa S.A.', 'financeiro@alfa.com', 'alfa.inc', '{noop}client123', true, 'FREE');
INSERT INTO user_roles (user_id, role) VALUES ('c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f', 'CLIENT');
INSERT INTO user_workspace (user_id, workspace_id) VALUES ('c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f', '11111111-1111-1111-1111-111111111111');

-- 2. Roberto Alencar
INSERT INTO users (id, name, email, username, password, enabled, plan_role) 
VALUES ('d4e5f6a7-b8c9-0d1e-2f3a-4b5c6d7e8f9a', 'Roberto Alencar', 'roberto.alencar@email.com', 'roberto.alencar', '{noop}client123', true, 'FREE');
INSERT INTO user_roles (user_id, role) VALUES ('d4e5f6a7-b8c9-0d1e-2f3a-4b5c6d7e8f9a', 'CLIENT');
INSERT INTO user_workspace (user_id, workspace_id) VALUES ('d4e5f6a7-b8c9-0d1e-2f3a-4b5c6d7e8f9a', '11111111-1111-1111-1111-111111111111');

-- 3. Juliana Maximino
INSERT INTO users (id, name, email, username, password, enabled, plan_role) 
VALUES ('e5f6a7b8-c9d0-1e2f-3a4b-5c6d7e8f9a0b', 'Juliana Maximino', 'ju.max@email.com', 'juliana.max', '{noop}client123', true, 'FREE');
INSERT INTO user_roles (user_id, role) VALUES ('e5f6a7b8-c9d0-1e2f-3a4b-5c6d7e8f9a0b', 'CLIENT');
INSERT INTO user_workspace (user_id, workspace_id) VALUES ('e5f6a7b8-c9d0-1e2f-3a4b-5c6d7e8f9a0b', '11111111-1111-1111-1111-111111111111');

-- Wallets
INSERT INTO wallet (id, name, created_at, user_id, workspace_id) 
VALUES ('a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6', 'Escritório Maximino & Associados', CURRENT_TIMESTAMP, 'fd4437c0-bda6-489d-964f-7e43169cace0', '11111111-1111-1111-1111-111111111111');
INSERT INTO wallet (id, name, created_at, user_id, workspace_id) 
VALUES ('b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e', 'Finanças Pessoais', CURRENT_TIMESTAMP, 'fd4437c0-bda6-489d-964f-7e43169cace0', '11111111-1111-1111-1111-111111111111');

-- Debts
INSERT INTO debt (id, wallet_id, client_id, user_id, name, email, description, amount, created_at, status, payment_type, workspace_id) 
VALUES ('f1a1b1c1-d1e1-4123-8123-a1b1c1d1e1f1', 'a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6', 'c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f', 'fd4437c0-bda6-489d-964f-7e43169cace0', 'Honorários Sucesso', 'financeiro@alfa.com', 'Ação Indenizatória Processo 001234-56', 15750.00, CURRENT_TIMESTAMP, 'OPEN', 'TRANSFER', '11111111-1111-1111-1111-111111111111');
INSERT INTO debt (id, wallet_id, client_id, user_id, name, email, description, amount, created_at, status, payment_type, workspace_id) 
VALUES ('f1a1b1c1-d1e1-4123-8123-a1b1c1d1e1f2', 'a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6', 'c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f', 'fd4437c0-bda6-489d-964f-7e43169cace0', 'Consultoria Mensal Jan/26', 'financeiro@alfa.com', 'Serviços de assessoria jurídica mensal', 2500.00, CURRENT_TIMESTAMP, 'OPEN', 'TRANSFER', '11111111-1111-1111-1111-111111111111');
INSERT INTO debt (id, wallet_id, client_id, user_id, name, email, description, amount, created_at, status, payment_type, workspace_id) 
VALUES ('f1a1b1c1-d1e1-4123-8123-a1b1c1d1e1f3', 'a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6', 'd4e5f6a7-b8c9-0d1e-2f3a-4b5c6d7e8f9a', 'fd4437c0-bda6-489d-964f-7e43169cace0', 'Divórcio Consensual', 'roberto.alencar@email.com', 'Honorários para elaboração de petição e acompanhamento', 3500.00, CURRENT_TIMESTAMP, 'OPEN', 'CASH', '11111111-1111-1111-1111-111111111111');
INSERT INTO debt (id, wallet_id, client_id, user_id, name, email, description, amount, created_at, status, payment_type, workspace_id) 
VALUES ('f1a1b1c1-d1e1-4123-8123-a1b1c1d1e1f4', 'b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e', 'e5f6a7b8-c9d0-1e2f-3a4b-5c6d7e8f9a0b', 'fd4437c0-bda6-489d-964f-7e43169cace0', 'Empréstimo Viagem', 'ju.max@email.com', 'Parcela 1/2 da viagem de final de ano', 600.00, CURRENT_TIMESTAMP, 'OPEN', 'CARD', '11111111-1111-1111-1111-111111111111');

-- Items
INSERT INTO items (id, debt_id, name, amount) 
VALUES (RANDOM_UUID(), 'f1a1b1c1-d1e1-4123-8123-a1b1c1d1e1f1', 'Honorários pro labore', 5750.00);
INSERT INTO items (id, debt_id, name, amount) 
VALUES (RANDOM_UUID(), 'f1a1b1c1-d1e1-4123-8123-a1b1c1d1e1f1', 'Honorários sucumbenciais', 10000.00);

-- Payments
INSERT INTO payment (id, debt_id, amount, date, type, created_at) 
VALUES (RANDOM_UUID(), 'f1a1b1c1-d1e1-4123-8123-a1b1c1d1e1f1', 5000.00, CURRENT_TIMESTAMP, 'TRANSFER', CURRENT_TIMESTAMP);
INSERT INTO payment (id, debt_id, amount, date, type, created_at) 
VALUES (RANDOM_UUID(), 'f1a1b1c1-d1e1-4123-8123-a1b1c1d1e1f3', 1000.00, CURRENT_TIMESTAMP, 'CASH', CURRENT_TIMESTAMP);
