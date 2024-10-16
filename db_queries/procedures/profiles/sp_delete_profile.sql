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