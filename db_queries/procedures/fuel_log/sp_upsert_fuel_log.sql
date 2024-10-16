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