create table if not exists  users (
  username VARCHAR(100),
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  password VARCHAR(255),
  enabled INT DEFAULT 1,
  created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
create unique index if not exists users_idx1 on users (
  username
);

create table if not exists  authorities (
  username VARCHAR(100),
  authority VARCHAR(100)
);

create unique index if not exists authorities_idx1 on authorities (
  username,
  authority
);

create table if not exists  messages (
  sender VARCHAR(100),
  receiver VARCHAR(100),
  message VARCHAR(255),
  message_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  is_read INT DEFAULT 0
);

create index if not exists messages_idx1 on messages (
  receiver,
  sender
);

create index if not exists messages_idx2 on messages (
  receiver,
  is_read
);