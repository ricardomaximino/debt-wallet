-- Run in PostgreSQL
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE debtor (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  name text NOT NULL,
  phone text,
  email text,
  notes text,
  created_at timestamp with time zone DEFAULT now()
);

CREATE TABLE item (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  name text NOT NULL,
  description text,
  date date,
  category text,
  created_at timestamp with time zone DEFAULT now()
);

CREATE TYPE debt_status AS ENUM ('OPEN','PAID','CANCELLED','LATE');
CREATE TYPE payment_type AS ENUM ('FULL','INSTALLMENTS','FLEXIBLE');

CREATE TABLE portfolio (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  name text NOT NULL,
  description text,
  created_at timestamp with time zone DEFAULT now()
);

CREATE TABLE debt (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  portfolio_id uuid NOT NULL REFERENCES portfolio(id) ON DELETE CASCADE,
  debtor_id uuid NOT NULL REFERENCES debtor(id),
  item_id uuid REFERENCES item(id),
  name text,
  note text,
  value numeric(18,2) NOT NULL,
  created_at timestamp with time zone DEFAULT now(),
  status debt_status NOT NULL DEFAULT 'OPEN',
  payment_type payment_type NOT NULL DEFAULT 'FLEXIBLE',
  remaining_balance numeric(18,2) NOT NULL,
  CONSTRAINT chk_remaining_nonneg CHECK (remaining_balance >= 0)
);

CREATE TABLE payment (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  debt_id uuid NOT NULL REFERENCES debt(id) ON DELETE CASCADE,
  amount numeric(18,2) NOT NULL CHECK (amount > 0),
  date timestamp with time zone DEFAULT now(),
  method text,
  note text
);

-- Indexes for queries
CREATE INDEX idx_debt_portfolio ON debt(portfolio_id);
CREATE INDEX idx_debt_debtor ON debt(debtor_id);
CREATE INDEX idx_payment_debt ON payment(debt_id);
