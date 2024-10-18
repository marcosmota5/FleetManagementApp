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
