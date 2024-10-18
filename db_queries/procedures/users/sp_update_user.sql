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