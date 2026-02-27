DELETE FROM payment;
DELETE FROM items;
DELETE FROM debt;
DELETE FROM wallet;
DELETE FROM user_workspace;
DELETE FROM workspace;
DELETE FROM user_roles;
DELETE FROM users;

-- Workspaces
-- 1. Ricardo's Private Workspace
-- 2. Maria's Private Workspace
-- 3. Shared Firm Workspace
INSERT INTO workspace (id, name, slug, owner_id) VALUES 
('11111111-1111-1111-1111-111111111111', 'Maximino Private Law', 'maximino-private', 'fd4437c0-bda6-489d-964f-7e43169cace0'),
('22222222-2222-2222-2222-222222222222', 'Maria Boutique Legal', 'maria-boutique', '77777777-7777-7777-7777-777777777777'),
('33333333-3333-3333-3333-333333333333', 'Metropolis Law Firm', 'metropolis-firm', 'fd4437c0-bda6-489d-964f-7e43169cace0');

-- Users
-- Ricardo (Lawyer)
INSERT INTO users (id, name, email, username, password, enabled, plan_role) 
VALUES ('fd4437c0-bda6-489d-964f-7e43169cace0', 'Ricardo Maximino', 'ricardo@email.com', 'admin', '{noop}password', true, 'PROFESSIONAL');
INSERT INTO user_roles (user_id, role) VALUES ('fd4437c0-bda6-489d-964f-7e43169cace0', 'ADMIN');

-- Maria (Lawyer)
INSERT INTO users (id, name, email, username, password, enabled, plan_role) 
VALUES ('77777777-7777-7777-7777-777777777777', 'Maria Silva', 'maria@email.com', 'maria', '{noop}password', true, 'PROFESSIONAL');
INSERT INTO user_roles (user_id, role) VALUES ('77777777-7777-7777-7777-777777777777', 'LAWYER');

-- New User (Needs Password Setup)
INSERT INTO users (id, name, email, username, password, enabled, plan_role) 
VALUES ('12345678-1234-1234-1234-1234567890ab', 'New User', 'new@email.com', 'newuser', '{noop}password', true, 'FREE');
INSERT INTO user_roles (user_id, role) VALUES ('12345678-1234-1234-1234-1234567890ab', 'LAWYER');
INSERT INTO user_workspace (user_id, workspace_id) VALUES ('12345678-1234-1234-1234-1234567890ab', '33333333-3333-3333-3333-333333333333');

-- Assignments
-- Ricardo has his private workspace and the firm one
INSERT INTO user_workspace (user_id, workspace_id) VALUES ('fd4437c0-bda6-489d-964f-7e43169cace0', '11111111-1111-1111-1111-111111111111');
INSERT INTO user_workspace (user_id, workspace_id) VALUES ('fd4437c0-bda6-489d-964f-7e43169cace0', '33333333-3333-3333-3333-333333333333');

-- Maria has her private workspace and the firm one
INSERT INTO user_workspace (user_id, workspace_id) VALUES ('77777777-7777-7777-7777-777777777777', '22222222-2222-2222-2222-222222222222');
INSERT INTO user_workspace (user_id, workspace_id) VALUES ('77777777-7777-7777-7777-777777777777', '33333333-3333-3333-3333-333333333333');

-- Wallets
-- 1. In Ricardo's Private
INSERT INTO wallet (id, name, created_at, user_id, workspace_id) 
VALUES (RANDOM_UUID(), 'Ricardo Local Cases', CURRENT_TIMESTAMP, 'fd4437c0-bda6-489d-964f-7e43169cace0', '11111111-1111-1111-1111-111111111111');

-- 2. In Maria's Private
INSERT INTO wallet (id, name, created_at, user_id, workspace_id) 
VALUES (RANDOM_UUID(), 'Maria Private Clients', CURRENT_TIMESTAMP, '77777777-7777-7777-7777-777777777777', '22222222-2222-2222-2222-222222222222');

-- 3. Shared Firm Wallet
INSERT INTO wallet (id, name, created_at, user_id, workspace_id) 
VALUES ('99999999-9999-9999-9999-999999999901', 'Metropolis Corporate Accounts', CURRENT_TIMESTAMP, 'fd4437c0-bda6-489d-964f-7e43169cace0', '33333333-3333-3333-3333-333333333333');

-- Clients
-- Alfa Corp (Client of the Firm)
INSERT INTO users (id, name, email, username, password, enabled, plan_role) 
VALUES ('c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f', 'Alfa Corp', 'finance@alfa.com', 'alfa', '{noop}password', true, 'FREE');
INSERT INTO user_roles (user_id, role) VALUES ('c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f', 'CLIENT');
INSERT INTO user_workspace (user_id, workspace_id) VALUES ('c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f', '33333333-3333-3333-3333-333333333333');

-- Debts in Shared Workspace
INSERT INTO debt (id, wallet_id, client_id, user_id, name, email, description, amount, created_at, status, payment_type, workspace_id) 
VALUES ('88888888-8888-8888-8888-888888888801', '99999999-9999-9999-9999-999999999901', 'c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f', 'fd4437c0-bda6-489d-964f-7e43169cace0', 'Quarterly Legal Fees', 'finance@alfa.com', 'Retainer for Q1 2026', 15000.00, CURRENT_TIMESTAMP, 'OPEN', 'TRANSFER', '33333333-3333-3333-3333-333333333333');

-- Payments
INSERT INTO payment (id, debt_id, amount, date, type, created_at) 
VALUES (RANDOM_UUID(), '88888888-8888-8888-8888-888888888801', 5000.00, CURRENT_TIMESTAMP, 'TRANSFER', CURRENT_TIMESTAMP);
