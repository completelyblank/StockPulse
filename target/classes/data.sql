-- Users
INSERT INTO "user" (id, username, password, balance, salary_amount, salary_interval_seconds)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'muhammad', 'pass123', 5000, 100, 60),
  ('22222222-2222-2222-2222-222222222222', 'ahmed', 'pass456', 3000, 50, 60),
  ('33333333-3333-3333-3333-333333333333', 'raza', 'pass789', 7000, 150, 60);

-- Stocks
INSERT INTO stock (symbol, name, price)
VALUES
  ('AAPL', 'Apple Inc.', 150.0),
  ('GOOGL', 'Alphabet Inc.', 2800.0),
  ('TSLA', 'Tesla Inc.', 700.0),
  ('AMZN', 'Amazon.com Inc.', 3300.0),
  ('MSFT', 'Microsoft Corporation', 290.0);

-- Stock Holdings
INSERT INTO stock_holding (id, user_id, stock_symbol, quantity)
VALUES
  ('00000000-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', 'AAPL', 5),
  ('00000000-0000-0000-0000-000000000002', '11111111-1111-1111-1111-111111111111', 'GOOGL', 2),
  ('00000000-0000-0000-0000-000000000003', '22222222-2222-2222-2222-222222222222', 'TSLA', 3),
  ('00000000-0000-0000-0000-000000000004', '33333333-3333-3333-3333-333333333333', 'AMZN', 1),
  ('00000000-0000-0000-0000-000000000005', '33333333-3333-3333-3333-333333333333', 'MSFT', 10);
