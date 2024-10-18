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
