CREATE VIEW vw_users
AS
SELECT 
    us.id AS 'user_id',
    us.first_name AS 'user_first_name',
    us.last_name AS 'user_last_name',
    us.sex AS 'user_sex',
    us.birth_date AS 'user_birth_date',
    us.phone_number AS 'user_phone_number',
    us.email AS 'user_email',
    us.login AS 'user_login',
    us.password_hash AS 'user_password_hash',
    us.salt AS 'user_salt',
    us.created_on AS 'user_created_on',
    prof.*
FROM tb_users AS us
INNER JOIN vw_profiles AS prof ON prof.profile_id = us.profile_id;