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