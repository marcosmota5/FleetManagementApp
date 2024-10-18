CREATE VIEW vw_vehicles
AS
SELECT 
    vehi.id AS 'vehicle_id',
    vehi.license_plate AS 'vehicle_license_plate',
    vehi.type AS 'vehicle_type',
    vehi.brand AS 'vehicle_brand',
    vehi.model AS 'vehicle_model',
    vehi.year AS 'vehicle_year',
    vehi.fuel_type AS 'vehicle_fuel_type',
    vehi.mileage AS 'vehicle_mileage',
    vehi.fuel_level AS 'vehicle_fuel_level',
    vehi.status AS 'vehicle_status',
    vehi.created_on AS 'vehicle_created_on',
    comp.*
FROM tb_vehicles AS vehi
INNER JOIN vw_companies AS comp ON comp.company_id = vehi.company_id;