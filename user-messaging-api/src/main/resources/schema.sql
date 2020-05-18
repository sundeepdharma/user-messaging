create table if not exists oauth_access_token (
  token_id VARCHAR(255),
  token BLOB,
  authentication_id VARCHAR(255) PRIMARY KEY,
  user_name VARCHAR(255),
  client_id VARCHAR(255),
  authentication BLOB,
  refresh_token VARCHAR(255)
);

create unique index if not exists oauth_access_token_idx1 on oauth_access_token (
  token_id
);

create table if not exists oauth_refresh_token (
  token_id VARCHAR(255),
  token BLOB,
  authentication BLOB
);

create table if not exists  oauth_client_details (
  client_id VARCHAR(255) PRIMARY KEY,
  resource_ids VARCHAR(255),
  client_secret VARCHAR(255),
  scope VARCHAR(255),
  authorized_grant_types VARCHAR(255),
  web_server_redirect_uri VARCHAR(255),
  authorities VARCHAR(255),
  access_token_validity INT,
  refresh_token_validity INT,
  additional_information VARCHAR(255),
  autoapprove VARCHAR(255)
);

create unique index if not exists oauth_client_details_idx1 on oauth_client_details (
  client_id
);

create table if not exists oauth_client_token (
  token_id VARCHAR(255),
  token LONGVARBINARY,
  authentication_id VARCHAR(255),
  user_name VARCHAR(255),
  client_id VARCHAR(255)
);

create table if not exists  oauth_code (
  code VARCHAR(255), authentication LONGVARBINARY
);

create table if not exists  oauth_approvals (
  userId VARCHAR(255),
  clientId VARCHAR(255),
  scope VARCHAR(255),
  status VARCHAR(10),
  expiresAt TIMESTAMP,
  lastModifiedAt TIMESTAMP
);

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