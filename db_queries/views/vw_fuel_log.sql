CREATE VIEW vw_fuel_log
AS
SELECT 
    fulo.id AS 'fuel_log_id',
    fulo.fuel_date AS 'fuel_log_fuel_date',
    fulo.liters_filled AS 'fuel_log_liters_filled',
    fulo.fuel_price_per_liter AS 'fuel_log_fuel_price_per_liter',
    fulo.total_cost AS 'fuel_log_total_cost',
    fulo.comments AS 'fuel_log_comments',
    vehi.*,
    us.*
FROM tb_fuel_log AS fulo
INNER JOIN vw_vehicles AS vehi ON vehi.vehicle_id = fulo.vehicle_id
INNER JOIN vw_users AS us ON us.user_id = fulo.user_id;