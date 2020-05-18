INSERT INTO oauth_client_details
  (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, autoapprove)
VALUES
  ('user-messaging-frontend', 'user-messaging', 'JdkjshdbksH$PPP87g987f', 'read,write', 'password,refresh_token', 'http://localhost:8081', 'read,write', 1800, 0, 'false');


INSERT INTO USERS
  ( username, first_name, last_name, password, enabled, created_time, updated_time)
VALUES
  ( 'admin','Test','Admin','administrator', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP );

INSERT INTO AUTHORITIES
  ( username, authority )
VALUES
  ( 'admin', 'ADMIN' );

INSERT INTO AUTHORITIES
  ( username, authority )
VALUES
  ( 'admin', 'USER' );

INSERT INTO USERS
  ( username, first_name, last_name, password, enabled, created_time, updated_time)
VALUES
  ( 'user1','Test', 'User','testuser', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP );


INSERT INTO AUTHORITIES
  ( username, authority )
VALUES
  ( 'user1', 'USER' );