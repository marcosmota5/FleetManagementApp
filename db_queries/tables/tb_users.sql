CREATE TABLE tb_users
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    sex ENUM('M', 'F') NOT NULL,
    birth_date DATE NOT NULL,
    phone_number VARCHAR(20),
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(50) NOT NULL UNIQUE,
    password_hash BINARY(64) NOT NULL,
    salt CHAR(36) NOT NULL,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profile_id INT NOT NULL,
    CONSTRAINT fk_tb_users_tb_profiles FOREIGN KEY (profile_id) REFERENCES tb_profiles (id) ON DELETE CASCADE
);