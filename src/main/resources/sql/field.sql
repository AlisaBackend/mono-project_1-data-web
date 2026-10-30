-- ===== Пользователи =====
INSERT INTO users_project (username, password, email, role) VALUES
('admin', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5B6/Bqg7ZkF6Q6T1VY9t5zV7lP9vK', 'admin@example.com', 'ROLE_ADMIN'),
('user1', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5B6/Bqg7ZkF6Q6T1VY9t5zV7lP9vK', 'user1@example.com', 'ROLE_USER'),
('user2', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5B6/Bqg7ZkF6Q6T1VY9t5zV7lP9vK', 'user2@example.com', 'ROLE_USER')
ON CONFLICT (username) DO NOTHING;

-- ===== Задачи =====
INSERT INTO tasks (title, description, completed, user_id) VALUES
('First task',  'Buy groceries',        FALSE, 2),
('Second task', 'Read a book',          TRUE,  2),
('Admin task',  'Configure server',     FALSE, 1),
('Third task',  'Write unit tests',     FALSE, 3);