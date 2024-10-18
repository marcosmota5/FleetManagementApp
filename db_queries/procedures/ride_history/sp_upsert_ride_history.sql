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
            comments
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
            p_comments
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