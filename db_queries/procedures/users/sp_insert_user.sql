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