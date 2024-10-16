CREATE TABLE tb_fuel_log
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    user_id INT NOT NULL,
    fuel_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    liters_filled DECIMAL(20,2),
    fuel_price_per_liter DECIMAL(20, 2),
    total_cost DECIMAL(20, 2),
    comments TEXT,
    CONSTRAINT fk_tb_fuel_log_tb_vehicles FOREIGN KEY (vehicle_id) REFERENCES tb_vehicles (id) ON DELETE CASCADE,
    CONSTRAINT fk_tb_fuel_log_tb_users FOREIGN KEY (user_id) REFERENCES tb_users (id) ON DELETE CASCADE
);