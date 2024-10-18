CREATE TABLE tb_vehicles
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    license_plate VARCHAR(10) NOT NULL UNIQUE,
    type ENUM('Car', 'Motorcycle', 'Truck') NOT NULL,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    year SMALLINT NOT NULL,
    fuel_type ENUM('Petrol', 'Diesel', 'Electric') NOT NULL,
    mileage DECIMAL(20,2) NOT NULL,
    fuel_level DECIMAL(20,2) NOT NULL,
    status ENUM('Available', 'Rented', 'Maintenance', 'Retired') NOT NULL,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    company_id INT NOT NULL,
    CONSTRAINT fk_tb_vehicles_tb_companies FOREIGN KEY (company_id) REFERENCES tb_companies (id) ON DELETE CASCADE
);