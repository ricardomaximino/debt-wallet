-- Clean up existing data to avoid conflicts on restart (optional for memory DB but good practice)
DELETE FROM payment;
DELETE FROM items;
DELETE FROM debt;
DELETE FROM debtor;
DELETE FROM wallet;
DELETE FROM users;

-- User: Ricardo Maximino (Solo Lawyer)
INSERT INTO users (id, name, email, plan_role) 
VALUES ('fd4437c0-bda6-489d-964f-7e43169cace0', 'Dr. Ricardo Maximino', 'ricardo@mail.com', 'FREE');

-- Wallets
INSERT INTO wallet (id, name, created_at, user_id) 
VALUES ('a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6', 'Escritório Maximino & Associados', CURRENT_TIMESTAMP, 'fd4437c0-bda6-489d-964f-7e43169cace0');
INSERT INTO wallet (id, name, created_at, user_id) 
VALUES ('b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e', 'Finanças Pessoais', CURRENT_TIMESTAMP, 'fd4437c0-bda6-489d-964f-7e43169cace0');

-- Debtors (Clients and Personal Contacts)
INSERT INTO debtor (id, name, surname, address, email, phone, observation, created_at) 
VALUES ('c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f', 'Incorporadora', 'Alfa S.A.', 'Av. Paulista, 1000', 'financeiro@alfa.com', '11999998888', 'Cliente corporativo recorrente', CURRENT_TIMESTAMP);
INSERT INTO debtor (id, name, surname, address, email, phone, observation, created_at) 
VALUES ('d4e5f6a7-b8c9-0d1e-2f3a-4b5c6d7e8f9a', 'Roberto', 'Alencar', 'Rua das Flores, 123', 'roberto.alencar@email.com', '11988887777', 'Caso cível em andamento', CURRENT_TIMESTAMP);
INSERT INTO debtor (id, name, surname, address, email, phone, observation, created_at) 
VALUES ('e5f6a7b8-c9d0-1e2f-3a4b-5c6d7e8f9a0b', 'Juliana', 'Maximino', 'Rua Augusta, 456', 'ju.max@email.com', '11977776666', 'Irmã - empréstimo pessoal', CURRENT_TIMESTAMP);

-- Debts (Professional Wallet)
INSERT INTO debt (id, wallet_id, debtor_id, user_id, name, email, description, amount, created_at, status, payment_type) 
VALUES ('f1a1b1c1-d1e1-4123-8123-a1b1c1d1e1f1', 'a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6', 'c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f', 'fd4437c0-bda6-489d-964f-7e43169cace0', 'Honorários Sucesso', 'financeiro@alfa.com', 'Ação Indenizatória Processo 001234-56', 15750.00, CURRENT_TIMESTAMP, 'OPEN', 'TRANSFER');
INSERT INTO debt (id, wallet_id, debtor_id, user_id, name, email, description, amount, created_at, status, payment_type) 
VALUES ('f1a1b1c1-d1e1-4123-8123-a1b1c1d1e1f2', 'a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6', 'c3d4e5f6-a7b8-9c0d-1e2f-3a4b5c6d7e8f', 'fd4437c0-bda6-489d-964f-7e43169cace0', 'Consultoria Mensal Jan/26', 'financeiro@alfa.com', 'Serviços de assessoria jurídica mensal', 2500.00, CURRENT_TIMESTAMP, 'OPEN', 'TRANSFER');
INSERT INTO debt (id, wallet_id, debtor_id, user_id, name, email, description, amount, created_at, status, payment_type) 
VALUES ('f1a1b1c1-d1e1-4123-8123-a1b1c1d1e1f3', 'a1b2c3d4-e5f6-7a8b-9c0d-e1f2a3b4c5d6', 'd4e5f6a7-b8c9-0d1e-2f3a-4b5c6d7e8f9a', 'fd4437c0-bda6-489d-964f-7e43169cace0', 'Divórcio Consensual', 'roberto.alencar@email.com', 'Honorários para elaboração de petição e acompanhamento', 3500.00, CURRENT_TIMESTAMP, 'OPEN', 'CASH');

-- Debts (Personal Wallet)
INSERT INTO debt (id, wallet_id, debtor_id, user_id, name, email, description, amount, created_at, status, payment_type) 
VALUES ('f1a1b1c1-d1e1-4123-8123-a1b1c1d1e1f4', 'b2c3d4e5-f6a7-8b9c-0d1e-2f3a4b5c6d7e', 'e5f6a7b8-c9d0-1e2f-3a4b-5c6d7e8f9a0b', 'fd4437c0-bda6-489d-964f-7e43169cace0', 'Empréstimo Viagem', 'ju.max@email.com', 'Parcela 1/2 da viagem de final de ano', 600.00, CURRENT_TIMESTAMP, 'OPEN', 'PIX');

-- Items (Breakdown of some debts if needed, though they aren't used much in UI yet)
INSERT INTO items (id, debt_id, name, amount) 
VALUES (UUID(), 'f1a1b1c1-d1e1-4123-8123-a1b1c1d1e1f1', 'Honorários pro labore', 5750.00);
INSERT INTO items (id, debt_id, name, amount) 
VALUES (UUID(), 'f1a1b1c1-d1e1-4123-8123-a1b1c1d1e1f1', 'Honorários sucumbenciais', 10000.00);

-- Payments
-- Alfa partially paid the success fees
INSERT INTO payment (id, debt_id, amount, date, type, created_at) 
VALUES (UUID(), 'f1a1b1c1-d1e1-4123-8123-a1b1c1d1e1f1', 5000.00, CURRENT_TIMESTAMP, 'TRANSFER', CURRENT_TIMESTAMP);
-- Roberto paid a sign-up fee
INSERT INTO payment (id, debt_id, amount, date, type, created_at) 
VALUES (UUID(), 'f1a1b1c1-d1e1-4123-8123-a1b1c1d1e1f3', 1000.00, CURRENT_TIMESTAMP, 'CASH', CURRENT_TIMESTAMP);
