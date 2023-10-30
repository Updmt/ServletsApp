CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name  VARCHAR(255)
);

CREATE TABLE files
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    text VARCHAR(255) NOT NULL,
    file_name VARCHAR(255) NOT NULL
);

CREATE TABLE events
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_type VARCHAR(255),
    created_at VARCHAR(255),
    user_id    BIGINT,
    file_id    BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (file_id) REFERENCES files(id)
);