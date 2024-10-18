CREATE VIEW vw_ride_history
AS
SELECT 
    rihi.id AS 'ride_history_id',
    rihi.start_location AS 'ride_history_start_location',
    rihi.end_location AS 'ride_history_end_location',
    rihi.start_date AS 'ride_history_start_date',
    rihi.end_date AS 'ride_history_end_date',
    rihi.kilometers_driven AS 'ride_history_kilometers_driven',
    rihi.fuel_consumed AS 'ride_history_fuel_consumed',
    rihi.comments AS 'ride_history_comments',
    vehi.*,
    us.*
FROM tb_ride_history AS rihi
INNER JOIN vw_vehicles AS vehi ON vehi.vehicle_id = rihi.vehicle_id
INNER JOIN vw_users AS us ON us.user_id = rihi.user_id;