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
