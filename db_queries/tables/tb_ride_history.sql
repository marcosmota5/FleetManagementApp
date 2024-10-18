CREATE TABLE tb_ride_history
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    user_id INT NOT NULL,
    start_location VARCHAR(100) NOT NULL,
    end_location VARCHAR(100),
    start_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_date DATETIME,
    kilometers_driven DECIMAL(20,2),
    fuel_consumed DECIMAL(20, 2),
    comments TEXT,
    CONSTRAINT fk_tb_ride_history_tb_vehicles FOREIGN KEY (vehicle_id) REFERENCES tb_vehicles (id) ON DELETE CASCADE,
    CONSTRAINT fk_tb_ride_history_tb_users FOREIGN KEY (user_id) REFERENCES tb_users (id) ON DELETE CASCADE
);