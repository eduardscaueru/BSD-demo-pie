create database users;

\connect users;

CREATE   TABLE user_table (
                              user_id SERIAL PRIMARY KEY,
                              user_name varchar(255),
                              email varchar(255),
                              password varchar(255)
);

CREATE  TABLE user_login (
                             user_login_id SERIAL PRIMARY KEY,
                             user_id INTEGER REFERENCES user_table(user_id),
                             token varchar(255),
                             token_expire_time varchar(255)
);

CREATE TABLE pie_slice_table(
                                   pie_slice_id SERIAL PRIMARY KEY,
                                   ticker varchar(10),
                                   invested_money FLOAT,
                                   shares FLOAT
);

CREATE TABLE pie(
                                   pie_slice_id INTEGER REFERENCES pie_slice_table(pie_slice_id),
                                   pie_name varchar(255),
                                   user_id INTEGER REFERENCES user_table(user_id)
);

CREATE  TABLE trade_info(
                            trade_id SERIAL PRIMARY KEY,
                            user_id INTEGER REFERENCES user_table(user_id),
                            pie_id INTEGER REFERENCES pie(pie_id),
                            stock_name varchar(255),
                            ticker varchar(10),
                            buy_sell varchar(5),
                            value  FLOAT,
                            shares FLOAT,
                            timestamp INTEGER
);

ALTER TABLE pie ADD PRIMARY KEY (pie_slice_id, pie_name);

CREATE ROLE new_user LOGIN PASSWORD 'new_user_password';
REVOKE CONNECT ON DATABASE users  FROM PUBLIC;
GRANT USAGE ON SCHEMA public TO new_user;
GRANT ALL PRIVILEGES ON DATABASE users TO new_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO new_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO new_user;

INSERT INTO user_table (user_name , email , password) VALUES ('admin', 'admin@admin.com', 'admin');