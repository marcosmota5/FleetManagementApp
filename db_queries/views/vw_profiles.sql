CREATE VIEW vw_profiles
AS
SELECT 
    prof.id AS 'profile_id',
    prof.name AS 'profile_name',
    prof.description AS 'profile_description',
    prof.priority AS 'profile_priority',
    prof.created_on AS 'profile_created_on'
FROM tb_profiles AS prof;