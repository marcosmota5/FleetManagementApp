-- DROP DATABASE IF EXISTS db_fleet_management;

-- CREATE DATABASE db_fleet_management;

-- USE db_fleet_management;

SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 ; 
 
CREATE TABLE tb_profiles
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(500),
    priority INT NOT NULL,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
); 
 
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
 
CREATE TABLE tb_companies
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
); 
 
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
 
DELIMITER $$

CREATE PROCEDURE sp_upsert_profile
(
    IN p_id INT,
    IN p_name VARCHAR(255),
    IN p_description VARCHAR(500),
	IN p_priority INT,
    OUT p_result_id INT
)
BEGIN

    -- Declares the data count and priority
    DECLARE data_count INT;
    DECLARE old_priority INT;
    DECLARE current_datetime DATETIME;

    -- Set the current datetime to be used in the operations in order to have the same for all records
    SET current_datetime = NOW();

    -- Check if it's an update operation
    IF p_id = 0 THEN

        -- Starts the transaction
        START TRANSACTION;

        -- Updates the current priorities
        UPDATE tb_profiles
        SET 
            priority = priority + 1
        WHERE priority >= p_priority;

        -- Perform the insert into the database
        INSERT INTO tb_profiles 
        (
            name,
            description,
            priority,
            created_on
        ) 
        VALUES 
        (
            p_name,
            p_description,
            p_priority,
            current_datetime
        );

        -- Set the result id
        SET p_result_id = LAST_INSERT_ID();

        -- Commit the changes to the database
        COMMIT;

    ELSE
 
        -- Set the data_count value
        SELECT COUNT(*) INTO data_count FROM tb_profiles WHERE id = p_id;

        -- Check if the data exists and, if not, throws an exception
        IF data_count = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Profile not found';
        END IF;
        
        -- Starts the transaction
        START TRANSACTION;

        -- Set the old_priority value
        SELECT priority INTO old_priority FROM tb_profiles WHERE id = p_id;
        
        -- If the old priority is greater than the new one
        IF old_priority > p_priority THEN
        
            -- Updates the current priorities
            UPDATE tb_profiles
            SET 
                priority = priority + 1
            WHERE priority >= p_priority AND priority < old_priority;
        
        END IF;
        
        -- If the old priority is less than the new one
        IF old_priority < p_priority THEN
        
            -- Updates the current priorities
            UPDATE tb_profiles
            SET 
                priority = priority - 1
            WHERE priority > old_priority AND priority <= p_priority;
        
        END IF;
        
        -- Perform the update into the database
        UPDATE tb_profiles
        SET
            name = p_name,
            description = p_description,
            priority = p_priority
        WHERE id = p_id;

        -- Set the result id
        SET p_result_id = p_id;

        -- Commit the changes to the database
        COMMIT;

    END IF;

END $$

DELIMITER ;
 
 
DELIMITER $$

CREATE PROCEDURE sp_delete_profile
(
    IN p_id INT
)
BEGIN

    -- Declares the data count
    DECLARE data_count INT;
    DECLARE current_datetime DATETIME;

    -- Set the data_count value
    SELECT COUNT(*) INTO data_count FROM tb_profiles WHERE id = p_id;

    -- Check if the data exists and, if not, throws an expection
    IF data_count = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Profile not found';
    END IF;

    -- Starts the transaction
    START TRANSACTION;

    -- Perform the delete into the database
    DELETE FROM tb_profiles
    WHERE id = p_id;

    -- Commit the changes to the database
    COMMIT;

END $$

DELIMITER ; 
 
DELIMITER $$

CREATE PROCEDURE sp_insert_user
(
    IN p_first_name VARCHAR(100),
    IN p_last_name VARCHAR(100),
    IN p_sex ENUM('M', 'F'),
    IN p_birth_date DATETIME,
    IN p_phone_number VARCHAR(20),
    IN p_email VARCHAR(255),
    IN p_login VARCHAR(50),
    IN p_password VARCHAR(64),
    IN p_profile_id INT,
    OUT p_result_id INT
)
BEGIN

    -- Declares the salt for the user
    DECLARE salt_value CHAR(36);
    DECLARE current_datetime DATETIME;

    -- Set the current datetime to be used in the operations in order to have the same for all records
    SET current_datetime = NOW();

    -- Set the salt value
    SELECT UUID() INTO salt_value;

    -- Starts the transaction
    START TRANSACTION;

    -- Perform the insert into the database
    INSERT INTO tb_users 
    (
        first_name,
        last_name,
        sex,
        birth_date,
        phone_number,
        email,
        login,
        password_hash,
        salt,
        created_on,
        profile_id
    ) 
    VALUES 
    (
        p_first_name,
        p_last_name,
        p_sex,
        p_birth_date,
        p_phone_number,
        p_email,
        p_login,
        SHA2(CONCAT(p_password, salt_value), 256),
        salt_value,
        current_datetime,
        p_profile_id
    );

    -- Set the last insert id
    SET p_result_id = LAST_INSERT_ID();

    -- Commit the changes to the database
    COMMIT;

END $$

DELIMITER ; 
 
DELIMITER $$

CREATE PROCEDURE sp_update_user
(
    IN p_id INT,
    IN p_first_name VARCHAR(100),
    IN p_last_name VARCHAR(100),
    IN p_sex ENUM('M', 'F'),
    IN p_birth_date DATETIME,
    IN p_phone_number VARCHAR(20),
    IN p_email VARCHAR(255),
    IN p_login VARCHAR(50),
    IN p_profile_id INT
)
BEGIN

    -- Declares the data count
    DECLARE data_count INT;
    DECLARE current_datetime DATETIME;

    -- Set the current datetime to be used in the operations in order to have the same for all records
    SET current_datetime = NOW();

    -- Set the data_count value
    SELECT COUNT(*) INTO data_count FROM tb_users WHERE id = p_id;

    -- Check if the data exists and, if not, throws an expection
    IF data_count = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User not found';
    END IF;

    -- Starts the transaction
    START TRANSACTION;

    -- Perform the update into the database
    UPDATE tb_users
    SET
        first_name = p_first_name,
        last_name = p_last_name,
        sex = p_sex,
        birth_date = p_birth_date,
        phone_number = p_phone_number,
        email = p_email,
        login = p_login,
        profile_id = p_profile_id
    WHERE id = p_id;

    -- Commit the changes to the database
    COMMIT;

END $$

DELIMITER ; 
 
DELIMITER $$

CREATE PROCEDURE sp_delete_user
(
    IN p_id INT
)
BEGIN

    -- Declares the data count
    DECLARE data_count INT;
    DECLARE current_datetime DATETIME;

    -- Set the data_count value
    SELECT COUNT(*) INTO data_count FROM tb_users WHERE id = p_id;

    -- Check if the data exists and, if not, throws an expection
    IF data_count = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User not found';
    END IF;

    -- Starts the transaction
    START TRANSACTION;

    -- Perform the delete into the database
    DELETE FROM tb_users
    WHERE id = p_id;

    -- Commit the changes to the database
    COMMIT;

END $$

DELIMITER ; 
 
DELIMITER $$

CREATE PROCEDURE sp_execute_login
(
    IN p_login_or_email VARCHAR(255),
    IN p_password VARCHAR(64),
    OUT p_result_id INT
)
BEGIN

    -- Declares the variables
    DECLARE target_user_user_id INT;
    DECLARE data_count INT;

    -- Set the data_count value
    SELECT COUNT(*) INTO data_count FROM tb_users WHERE login = p_login_or_email OR email = p_login_or_email;

    -- Check if the user exist and, if not, throws an exception
    IF data_count = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User not found';
    END IF;

    -- Sets the variables
    SELECT 
        id INTO target_user_user_id
    FROM tb_users
    WHERE (login = p_login_or_email OR email = p_login_or_email) AND password_hash = SHA2(CONCAT(p_password, salt), 256);

    -- Check if the provided password is correct and, if not, throws an exception
    IF target_user_user_id IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Incorrect password';
    END IF;

    -- Sets the id of the user in the out parameter
    SET p_result_id = target_user_user_id;

END $$

DELIMITER ; 
 
DELIMITER $$

CREATE PROCEDURE sp_upsert_company
(
    IN p_id INT,
    IN p_code VARCHAR(100),
    IN p_name VARCHAR(255),
    OUT p_result_id INT
)
BEGIN

    -- Declares the data count
    DECLARE data_count INT;
    DECLARE current_datetime DATETIME;

    -- Set the current datetime to be used in the operations in order to have the same for all records
    SET current_datetime = NOW();

    -- Checks if the id is 0 and, if so, insert a new value, if not, update the existing values
    IF p_id = 0 THEN
 
        -- Starts the transaction
        START TRANSACTION;

        -- Perform the insert into the database
        INSERT INTO tb_companies 
        (
            code,
            name,
            created_on
        ) 
        VALUES 
        (
            p_code,
            p_name,
            current_datetime
        );

        -- Set the result id
        SET p_result_id = LAST_INSERT_ID();

        -- Commit the changes to the database
        COMMIT;

    ELSE

        -- Set the data_count value
        SELECT COUNT(*) INTO data_count FROM tb_companies WHERE id = p_id;

        -- Check if the data exists and, if not, throws an exception
        IF data_count = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Company not found';
        END IF;
        
        -- Starts the transaction
        START TRANSACTION;

        -- Perform the update into the database
        UPDATE tb_companies
        SET
            code = p_code,
            name = p_name
        WHERE id = p_id;

        -- Set the result id
        SET p_result_id = p_id;

        -- Commit the changes to the database
        COMMIT;

    END IF;

END $$

DELIMITER ;
 
 
DELIMITER $$

CREATE PROCEDURE sp_delete_company
(
    IN p_id INT
)
BEGIN

    -- Declares the data count
    DECLARE data_count INT;
    DECLARE current_datetime DATETIME;

    -- Set the data_count value
    SELECT COUNT(*) INTO data_count FROM tb_companies WHERE id = p_id;

    -- Check if the data exists and, if not, throws an expection
    IF data_count = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Company not found';
    END IF;

    -- Starts the transaction
    START TRANSACTION;

    -- Perform the delete into the database
    DELETE FROM tb_companies
    WHERE id = p_id;

    -- Commit the changes to the database
    COMMIT;

END $$

DELIMITER ; 
 
DELIMITER $$

CREATE PROCEDURE sp_upsert_vehicle
(
    IN p_id INT,
    IN p_license_plate VARCHAR(10),
    IN p_type ENUM('Car', 'Motorcycle', 'Truck'),
    IN p_brand VARCHAR(100),
    IN p_model VARCHAR(100),
    IN p_year SMALLINT,
    IN p_fuel_type ENUM('Petrol', 'Diesel', 'Electric'),
    IN p_mileage DECIMAL(20,2),
    IN p_fuel_level DECIMAL(20,2),
    IN p_status ENUM('Available', 'Rented', 'Maintenance', 'Retired'),
    IN p_company_id INT,
    OUT p_result_id INT
)
BEGIN

    -- Declares the data count
    DECLARE data_count INT;
    DECLARE current_datetime DATETIME;

    -- Set the current datetime to be used in the operations in order to have the same for all records
    SET current_datetime = NOW();

    -- Checks if the id is 0 and, if so, insert a new value, if not, update the existing values
    IF p_id = 0 THEN
 
        -- Starts the transaction
        START TRANSACTION;

        -- Perform the insert into the database
        INSERT INTO tb_vehicles 
        (
            license_plate,
            type,
            brand,
            model,
            year,
            fuel_type,
            mileage,
            fuel_level,
            status,
            company_id,
            created_on
        ) 
        VALUES 
        (
            p_license_plate,
            p_type,
            p_brand,
            p_model,
            p_year,
            p_fuel_type,
            p_mileage,
            p_fuel_level,
            p_status,
            p_company_id,
            current_datetime
        );

        -- Set the result id
        SET p_result_id = LAST_INSERT_ID();

        -- Commit the changes to the database
        COMMIT;

    ELSE

        -- Set the data_count value
        SELECT COUNT(*) INTO data_count FROM tb_vehicles WHERE id = p_id;

        -- Check if the data exists and, if not, throws an exception
        IF data_count = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Vehicle not found';
        END IF;
        
        -- Starts the transaction
        START TRANSACTION;

        -- Perform the update into the database
        UPDATE tb_vehicles
        SET
            license_plate = p_license_plate,
            type = p_type,
            brand = p_brand,
            model = p_model,
            year = p_year,
            fuel_type = p_fuel_type,
            mileage = p_mileage,
            fuel_level = p_fuel_level,
            status = p_status,
            company_id = p_company_id
        WHERE id = p_id;

        -- Set the result id
        SET p_result_id = p_id;

        -- Commit the changes to the database
        COMMIT;

    END IF;

END $$

DELIMITER ;
 
 
DELIMITER $$

CREATE PROCEDURE sp_delete_vehicle
(
    IN p_id INT
)
BEGIN

    -- Declares the data count
    DECLARE data_count INT;
    DECLARE current_datetime DATETIME;

    -- Set the data_count value
    SELECT COUNT(*) INTO data_count FROM tb_vehicles WHERE id = p_id;

    -- Check if the data exists and, if not, throws an expection
    IF data_count = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Vehicle not found';
    END IF;

    -- Starts the transaction
    START TRANSACTION;

    -- Perform the delete into the database
    DELETE FROM tb_vehicles
    WHERE id = p_id;

    -- Commit the changes to the database
    COMMIT;

END $$

DELIMITER ; 
 
DELIMITER $$

CREATE PROCEDURE sp_upsert_ride_history
(
    IN p_id INT,
    IN p_vehicle_id INT,
    IN p_user_id INT,
    IN p_start_location VARCHAR(100),
    IN p_end_location VARCHAR(100),
    IN p_start_date DATETIME,
    IN p_end_date DATETIME,
    IN p_kilometers_driven DECIMAL(20,2),
    IN p_fuel_consumed DECIMAL(20, 2),
    IN p_comments TEXT,
    OUT p_result_id INT
)
BEGIN

    -- Declares the data count
    DECLARE data_count INT;
    DECLARE current_datetime DATETIME;

    -- Set the current datetime to be used in the operations in order to have the same for all records
    SET current_datetime = NOW();

    -- Checks if the id is 0 and, if so, insert a new value, if not, update the existing values
    IF p_id = 0 THEN
 
        -- Starts the transaction
        START TRANSACTION;

        -- Perform the insert into the database
        INSERT INTO tb_ride_history
        (
            vehicle_id,
            user_id,
            start_location,
            end_location,
            start_date,
            end_date,
            kilometers_driven,
            fuel_consumed,
            comments,
            created_on
        ) 
        VALUES 
        (
            p_vehicle_id,
            p_user_id,
            p_start_location,
            p_end_location,
            p_start_date,
            p_end_date,
            p_kilometers_driven,
            p_fuel_consumed,
            p_comments,
            current_datetime
        );

        -- Set the result id
        SET p_result_id = LAST_INSERT_ID();

        -- Commit the changes to the database
        COMMIT;

    ELSE

        -- Set the data_count value
        SELECT COUNT(*) INTO data_count FROM tb_ride_history WHERE id = p_id;

        -- Check if the data exists and, if not, throws an exception
        IF data_count = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ride history not found';
        END IF;
        
        -- Starts the transaction
        START TRANSACTION;

        -- Perform the update into the database
        UPDATE tb_ride_history
        SET
            vehicle_id = p_vehicle_id,
            user_id = p_user_id,
            start_location = p_start_location,
            end_location = p_end_location,
            start_date = p_start_date,
            end_date = p_end_date,
            kilometers_driven = p_kilometers_driven,
            fuel_consumed = p_fuel_consumed,
            comments = p_comments
        WHERE id = p_id;

        -- Set the result id
        SET p_result_id = p_id;

        -- Commit the changes to the database
        COMMIT;

    END IF;

END $$

DELIMITER ; 
 
DELIMITER $$

CREATE PROCEDURE sp_delete_ride_history
(
    IN p_id INT
)
BEGIN

    -- Declares the data count
    DECLARE data_count INT;
    DECLARE current_datetime DATETIME;

    -- Set the data_count value
    SELECT COUNT(*) INTO data_count FROM tb_ride_history WHERE id = p_id;

    -- Check if the data exists and, if not, throws an expection
    IF data_count = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ride history not found';
    END IF;

    -- Starts the transaction
    START TRANSACTION;

    -- Perform the delete into the database
    DELETE FROM tb_ride_history
    WHERE id = p_id;

    -- Commit the changes to the database
    COMMIT;

END $$

DELIMITER ; 
 
DELIMITER $$

CREATE PROCEDURE sp_upsert_fuel_log
(
    IN p_id INT,
    IN p_vehicle_id INT,
    IN p_user_id INT,
    IN p_fuel_date DATETIME,
    IN p_liters_filled DECIMAL(20,2),
    IN p_fuel_price_per_liter DECIMAL(20, 2),
    IN p_total_cost DECIMAL(20, 2),
    IN p_comments TEXT,
    OUT p_result_id INT
)
BEGIN

    -- Declares the data count
    DECLARE data_count INT;
    DECLARE current_datetime DATETIME;

    -- Set the current datetime to be used in the operations in order to have the same for all records
    SET current_datetime = NOW();

    -- Checks if the id is 0 and, if so, insert a new value, if not, update the existing values
    IF p_id = 0 THEN
 
        -- Starts the transaction
        START TRANSACTION;

        -- Perform the insert into the database
        INSERT INTO tb_fuel_log
        (
            vehicle_id,
            user_id,
            fuel_date,
            liters_filled,
            fuel_price_per_liter,
            total_cost,
            comments,
            created_on
        ) 
        VALUES 
        (
            p_vehicle_id,
            p_user_id,
            p_fuel_date,
            p_liters_filled,
            p_fuel_price_per_liter,
            p_total_cost,
            p_comments,
            current_datetime
        );

        -- Set the result id
        SET p_result_id = LAST_INSERT_ID();

        -- Commit the changes to the database
        COMMIT;

    ELSE

        -- Set the data_count value
        SELECT COUNT(*) INTO data_count FROM tb_fuel_log WHERE id = p_id;

        -- Check if the data exists and, if not, throws an exception
        IF data_count = 0 THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Fuel log not found';
        END IF;
        
        -- Starts the transaction
        START TRANSACTION;

        -- Perform the update into the database
        UPDATE tb_fuel_log
        SET
            vehicle_id = p_vehicle_id,
            user_id = p_user_id,
            fuel_date = p_fuel_date,
            liters_filled = p_liters_filled,
            fuel_price_per_liter = p_fuel_price_per_liter,
            total_cost = p_total_cost,
            comments = p_comments
        WHERE id = p_id;

        -- Set the result id
        SET p_result_id = p_id;

        -- Commit the changes to the database
        COMMIT;

    END IF;

END $$

DELIMITER ; 
 
DELIMITER $$

CREATE PROCEDURE sp_delete_fuel_log
(
    IN p_id INT
)
BEGIN

    -- Declares the data count
    DECLARE data_count INT;
    DECLARE current_datetime DATETIME;

    -- Set the data_count value
    SELECT COUNT(*) INTO data_count FROM tb_fuel_log WHERE id = p_id;

    -- Check if the data exists and, if not, throws an expection
    IF data_count = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Fuel log not found';
    END IF;

    -- Starts the transaction
    START TRANSACTION;

    -- Perform the delete into the database
    DELETE FROM tb_fuel_log
    WHERE id = p_id;

    -- Commit the changes to the database
    COMMIT;

END $$

DELIMITER ; 
 
-- Insert the admin profile
CALL sp_upsert_profile
(
  0, -- id
  'Administrator', -- name
  'Has all privileges', -- description
  1, -- priority
  @out_parameter -- id created
);

-- Insert the normal user profile
CALL sp_upsert_profile
(
  0, -- id
  'Advanced User', -- name
  'Has some privileges', -- description
  2, -- priority
  @out_parameter -- id created
);

-- Insert the normal user profile
CALL sp_upsert_profile
(
  0, -- id
  'Normal User', -- name
  'Has few privileges', -- description
  3, -- priority
  @out_parameter -- id created
);

-- Inser the master admin user
CALL sp_insert_user
(
  'Administrator', -- first name
  '', -- last name
  'M', -- sex
  '1990-01-01', -- birth date
  '0000000000000', -- phone number
  'admin@proreports.com.br', -- email
  'admin', -- login
  'Admin@123', -- password
  1, -- profile id
  @out_parameter -- id created
);

-- Inser the an advanced user
CALL sp_insert_user
(
  'Marcos', -- first name
  'Oliveira Mota', -- last name
  'M', -- sex
  '1993-10-05', -- birth date
  '4378709786', -- phone number
  'marcos.mota@marcosmota.tech', -- email
  'marcos.mota', -- login
  'Marcos@123', -- password
  1, -- profile id
  @out_parameter -- id created
);

-- Insert the company
CALL sp_upsert_company
(
  0, -- id
  '00001', -- code
  'Fleet Company', -- name
  @out_parameter
);

-- Insert some dummy vehicles
INSERT INTO tb_vehicles 
(license_plate, type, brand, model, year, fuel_type, mileage, fuel_level, status, company_id)
VALUES
('ABCD123', 'Car', 'Toyota', 'Corolla', 2018, 'Petrol', 102340.50, 35.00, 'Available', 1),
('EFGH456', 'Car', 'Honda', 'Civic', 2020, 'Petrol', 65320.75, 50.00, 'Rented', 1),
('IJKL789', 'Truck', 'Ford', 'F-150', 2019, 'Diesel', 134500.00, 75.00, 'Maintenance', 1),
('MNOP234', 'Motorcycle', 'Harley-Davidson', 'Iron 883', 2021, 'Petrol', 20500.30, 12.50, 'Available', 1),
('QRST567', 'Car', 'Tesla', 'Model 3', 2022, 'Electric', 31200.90, 90.00, 'Rented', 1),
('UVWX890', 'Truck', 'Chevrolet', 'Silverado', 2018, 'Diesel', 198000.00, 60.00, 'Retired', 1),
('YZA1234', 'Motorcycle', 'Yamaha', 'MT-09', 2020, 'Petrol', 15400.00, 10.00, 'Available', 1),
('BCDE567', 'Car', 'Nissan', 'Rogue', 2017, 'Petrol', 116500.40, 30.00, 'Rented', 1),
('FGHI890', 'Car', 'BMW', 'X5', 2019, 'Diesel', 85450.60, 45.00, 'Maintenance', 1),
('JKLM234', 'Truck', 'Ram', '1500', 2021, 'Petrol', 55200.25, 70.00, 'Available', 1);

-- Insert some dummy ride history
INSERT INTO tb_ride_history 
(vehicle_id, user_id, start_location, end_location, start_date, end_date, kilometers_driven, fuel_consumed, comments)
VALUES
(1, 1, 'Toronto', 'Mississauga', '2024-10-10 08:00:00', '2024-10-10 09:00:00', 30.50, 2.5, 'Smooth ride in morning traffic.'),
(2, 2, 'Hamilton', 'Toronto', '2024-09-15 12:00:00', '2024-09-15 13:30:00', 70.20, 5.0, NULL),
(3, 1, 'Ottawa', 'Montreal', '2024-08-20 06:30:00', '2024-08-20 09:45:00', 190.75, 20.0, 'Long haul delivery.'),
(4, 2, 'Waterloo', 'Guelph', '2024-10-12 15:00:00', '2024-10-12 15:45:00', 40.00, 1.8, 'Quick weekend ride.'),
(5, 1, 'Toronto', 'Brampton', '2024-07-11 08:30:00', '2024-07-11 09:00:00', 25.00, 0.0, 'Tesla trip, no fuel.'),
(6, 1, 'Barrie', 'Toronto', '2024-08-01 17:30:00', '2024-08-01 19:00:00', 90.00, 8.5, 'Heavy traffic during rush hour.'),
(7, 2, 'Kingston', 'Ottawa', '2024-09-05 07:00:00', '2024-09-05 09:30:00', 130.00, 7.2, NULL),
(8, 2, 'Windsor', 'London', '2024-08-22 14:00:00', '2024-08-22 16:30:00', 190.00, 11.0, 'Smooth ride on the highway.'),
(9, 1, 'Toronto', 'Niagara Falls', '2024-09-20 08:00:00', '2024-09-20 10:00:00', 130.00, 6.8, 'Tourist trip.'),
(10, 2, 'Thunder Bay', 'Sault Ste. Marie', '2024-09-14 06:00:00', '2024-09-14 12:00:00', 440.00, 22.0, 'Long-distance delivery.'),
(1, 1, 'Mississauga', 'Hamilton', '2024-07-22 10:30:00', '2024-07-22 11:45:00', 50.50, 4.0, NULL),
(2, 2, 'Toronto', 'Markham', '2024-09-17 18:00:00', '2024-09-17 18:45:00', 25.00, 1.2, 'Evening commute.'),
(3, 1, 'Ottawa', 'Kingston', '2024-10-02 05:30:00', '2024-10-02 07:15:00', 140.00, 12.5, 'Morning delivery.'),
(4, 2, 'Guelph', 'Kitchener', '2024-08-18 10:15:00', '2024-08-18 10:45:00', 20.00, 1.0, 'Quick ride for a meeting.'),
(5, 1, 'Brampton', 'Oakville', '2024-07-30 12:00:00', '2024-07-30 13:00:00', 45.00, 0.0, NULL),
(6, 2, 'Niagara Falls', 'Toronto', '2024-08-07 15:00:00', '2024-08-07 17:30:00', 125.00, 7.8, 'Highway trip with some traffic.'),
(7, 1, 'Ottawa', 'Toronto', '2024-09-25 05:00:00', '2024-09-25 12:00:00', 450.00, 30.0, 'Long trip with several stops.'),
(8, 2, 'London', 'Toronto', '2024-10-13 09:00:00', '2024-10-13 12:00:00', 200.00, 9.5, NULL),
(9, 1, 'Markham', 'Toronto', '2024-10-10 07:30:00', '2024-10-10 08:15:00', 30.00, 2.0, 'Smooth commute.'),
(10, 2, 'Sarnia', 'Windsor', '2024-09-06 13:00:00', '2024-09-06 15:00:00', 100.00, 6.5, 'Day trip.'),
(1, 2, 'Toronto', 'Mississauga', '2024-09-01 07:00:00', '2024-09-01 07:45:00', 30.00, 2.0, NULL),
(3, 1, 'Toronto', 'Montreal', '2024-07-01 04:00:00', '2024-07-01 10:00:00', 540.00, 50.0, 'Heavy cargo delivery.'),
(7, 2, 'Kingston', 'Toronto', '2024-10-05 06:30:00', '2024-10-05 10:15:00', 270.00, 15.0, 'Morning drive with light traffic.'),
(5, 1, 'Toronto', 'Guelph', '2024-08-25 15:00:00', '2024-08-25 17:00:00', 100.00, 0.0, 'Electric vehicle trip.'),
(9, 2, 'Thunder Bay', 'Sudbury', '2024-09-09 07:00:00', '2024-09-09 12:30:00', 500.00, 20.0, 'Long-distance business trip.'),
(6, 2, 'Burlington', 'Toronto', '2024-08-15 11:30:00', '2024-08-15 12:30:00', 55.00, 3.5, 'Smooth ride.'),
(8, 1, 'London', 'Guelph', '2024-07-17 09:00:00', '2024-07-17 10:30:00', 90.00, 5.0, 'Mid-morning trip.'),
(4, 1, 'Kitchener', 'Waterloo', '2024-07-10 14:00:00', '2024-07-10 14:15:00', 5.00, 0.3, 'Short ride.'),
(10, 2, 'Sault Ste. Marie', 'Thunder Bay', '2024-09-15 05:30:00', '2024-09-15 11:30:00', 400.00, 18.5, 'Long-distance delivery.'),
(2, 1, 'Toronto', 'Ottawa', '2024-10-14 07:00:00', '2024-10-14 12:30:00', 450.00, 20.0, 'Comfortable ride with Tesla.');

-- Inser tsome dummy fuel log
INSERT INTO tb_fuel_log 
(vehicle_id, user_id, fuel_date, liters_filled, fuel_price_per_liter, total_cost, comments)
VALUES
(1, 1, '2024-10-09 18:00:00', 40.00, 1.65, 66.00, 'Filled up before the morning trip to Mississauga.'),
(2, 2, '2024-09-15 11:45:00', 50.00, 1.60, 80.00, 'Topped off before heading to Toronto.'),
(3, 1, '2024-08-19 20:00:00', 100.00, 1.55, 155.00, 'Prepped for a long-distance delivery to Montreal.'),
(4, 2, '2024-10-12 14:30:00', 15.00, 1.70, 25.50, 'Quick fill-up for a short ride.'),
(6, 1, '2024-07-31 09:00:00', 60.00, 1.63, 97.80, 'Filled up after returning from Toronto.'),
(7, 2, '2024-09-05 17:00:00', 70.00, 1.58, 110.60, 'After arriving from Kingston.'),
(8, 2, '2024-08-22 17:00:00', 80.00, 1.55, 124.00, 'Post-trip fill-up in London.'),
(9, 1, '2024-09-19 19:30:00', 50.00, 1.67, 83.50, 'After visiting Niagara Falls.'),
(10, 2, '2024-09-14 13:00:00', 120.00, 1.53, 183.60, 'Filled up before the long delivery to Thunder Bay.'),
(1, 1, '2024-07-22 09:00:00', 30.00, 1.65, 49.50, 'Filled up in Hamilton before heading back to Toronto.'),
(3, 1, '2024-07-02 15:00:00', 80.00, 1.58, 126.40, 'Heavy cargo trip refueling.'),
(6, 2, '2024-08-07 18:00:00', 50.00, 1.62, 81.00, 'Topped off after heavy traffic drive.'),
(7, 1, '2024-09-25 18:00:00', 120.00, 1.54, 184.80, 'Filled up after long drive from Ottawa.'),
(8, 2, '2024-10-13 14:30:00', 90.00, 1.59, 143.10, 'Refueled after arriving from London.'),
(9, 1, '2024-10-10 18:00:00', 40.00, 1.66, 66.40, 'Before the commute to Toronto.'),
(10, 2, '2024-09-16 07:00:00', 100.00, 1.57, 157.00, 'Refueled before long drive to Sault Ste. Marie.'),
(4, 2, '2024-08-18 16:00:00', 10.00, 1.70, 17.00, 'Filled up for a local trip.'),
(6, 2, '2024-08-16 12:00:00', 60.00, 1.62, 97.20, 'After arriving in Toronto.'),
(7, 1, '2024-09-06 11:30:00', 100.00, 1.55, 155.00, 'Post-trip refill in Kingston.'),
(2, 1, '2024-10-14 18:00:00', 90.00, 1.60, 144.00, 'Long trip refuel in Ottawa.');

SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS ; 
 
