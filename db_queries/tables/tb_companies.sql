CREATE TABLE tb_companies
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);