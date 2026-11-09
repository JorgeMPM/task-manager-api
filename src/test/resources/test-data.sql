-- Vacia las tablas antes de insertar: el ALTER TABLE del final hace commit en H2,
-- asi que el rollback del test no deshace estos datos. Orden: primero las tablas que dependen de otras.
DELETE FROM task_tag;
DELETE FROM task;
DELETE FROM tag;
DELETE FROM category;

INSERT INTO category (id, name, description) VALUES
(1, 'Backend', 'Tareas de backend'),
(2, 'Frontend', 'Tareas de frontend');

INSERT INTO task (id, title, description, status, priority, due_date, created_at, category_id) VALUES
(1, 'Tarea PENDING alta', 'Descripcion', 'PENDING', 'HIGH', '2026-06-01', NOW(), 1),
(2, 'Tarea IN_PROGRESS media', 'Descripcion', 'IN_PROGRESS', 'MEDIUM', '2026-06-15', NOW(), 1),
(3, 'Tarea DONE baja', 'Descripcion', 'DONE', 'LOW', '2026-01-01', NOW(), 2);

INSERT INTO tag (id, name) VALUES
(1, 'urgente'),
(2, 'bug');

INSERT INTO task_tag (task_id, tag_id) VALUES
(1, 1), (1, 2), (2, 1);

-- Reinicia los contadores de ids: los INSERT llevan ids explicitos y el contador de H2 seguiria en 1.
ALTER TABLE task ALTER COLUMN id RESTART WITH 100;
ALTER TABLE category ALTER COLUMN id RESTART WITH 100;
ALTER TABLE tag ALTER COLUMN id RESTART WITH 100;