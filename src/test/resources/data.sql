INSERT INTO users (id, first_name, last_name, email, password, created_at) VALUES
  (10, 'Robert', 'Lock', 'lock@mail.com', 'password3', '2022-04-30'),
  (11, 'Luka', 'Petrov', 'Lucka@mail.com', 'password', '2022-04-30');

INSERT INTO labels (id, name, created_at) VALUES
  (12, 'firstLabel', '2022-04-30'),
  (13, 'secondLabel', '2022-04-30');

INSERT INTO task_status (id, name, created_at) VALUES
  (14, 'firstStatus', '2022-04-30'),
  (15, 'secondStatus', '2022-04-30');

INSERT INTO task (id, name, description, author_id, executor_id, task_status_id, created_at) VALUES
  (16, 'firstTask', 'task description', 10, 11, 14, '2022-04-30'),
  (17, 'secondTask', 'task description', 11, 10, 15, '2022-04-30');

INSERT INTO task_labels (task_id, labels_id) VALUES
  (16, 12);