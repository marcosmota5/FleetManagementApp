-- Insert the admin profile
CALL sp_upsert_profile
(
  0, -- id
  'Administrator', -- name
  'Has all privileges', -- description
  1, -- priority
  @out_parameter -- id created
);

-- Insert the normal user profile
CALL sp_upsert_profile
(
  0, -- id
  'Advanced User', -- name
  'Has some privileges', -- description
  2, -- priority
  @out_parameter -- id created
);

-- Insert the normal user profile
CALL sp_upsert_profile
(
  0, -- id
  'Normal User', -- name
  'Has few privileges', -- description
  3, -- priority
  @out_parameter -- id created
);

-- Inser the master admin user
CALL sp_insert_user
(
  'Administrator', -- first name
  '', -- last name
  'M', -- sex
  '1990-01-01', -- birth date
  '0000000000000', -- phone number
  'admin@proreports.com.br', -- email
  'admin', -- login
  'Admin@123', -- password
  1, -- profile id
  @out_parameter -- id created
);

-- Inser the an advanced user
CALL sp_insert_user
(
  'Marcos', -- first name
  'Oliveira Mota', -- last name
  'M', -- sex
  '1993-10-05', -- birth date
  '4378709786', -- phone number
  'marcos.mota@marcosmota.tech', -- email
  'marcos.mota', -- login
  'Marcos@123', -- password
  1, -- profile id
  @out_parameter -- id created
);

-- Insert the company
CALL sp_upsert_company
(
  0, -- id
  '00001', -- code
  'Fleet Company', -- name
  @out_parameter
);

-- Insert some dummy vehicles
INSERT INTO tb_vehicles 
(license_plate, type, brand, model, year, fuel_type, mileage, fuel_level, status, company_id)
VALUES
('ABCD123', 'Car', 'Toyota', 'Corolla', 2018, 'Petrol', 102340.50, 35.00, 'Available', 1),
('EFGH456', 'Car', 'Honda', 'Civic', 2020, 'Petrol', 65320.75, 50.00, 'Rented', 1),
('IJKL789', 'Truck', 'Ford', 'F-150', 2019, 'Diesel', 134500.00, 75.00, 'Maintenance', 1),
('MNOP234', 'Motorcycle', 'Harley-Davidson', 'Iron 883', 2021, 'Petrol', 20500.30, 12.50, 'Available', 1),
('QRST567', 'Car', 'Tesla', 'Model 3', 2022, 'Electric', 31200.90, 90.00, 'Rented', 1),
('UVWX890', 'Truck', 'Chevrolet', 'Silverado', 2018, 'Diesel', 198000.00, 60.00, 'Retired', 1),
('YZA1234', 'Motorcycle', 'Yamaha', 'MT-09', 2020, 'Petrol', 15400.00, 10.00, 'Available', 1),
('BCDE567', 'Car', 'Nissan', 'Rogue', 2017, 'Petrol', 116500.40, 30.00, 'Rented', 1),
('FGHI890', 'Car', 'BMW', 'X5', 2019, 'Diesel', 85450.60, 45.00, 'Maintenance', 1),
('JKLM234', 'Truck', 'Ram', '1500', 2021, 'Petrol', 55200.25, 70.00, 'Available', 1);

-- Insert some dummy ride history
INSERT INTO tb_ride_history 
(vehicle_id, user_id, start_location, end_location, start_date, end_date, kilometers_driven, fuel_consumed, comments)
VALUES
(1, 1, 'Toronto', 'Mississauga', '2024-10-10 08:00:00', '2024-10-10 09:00:00', 30.50, 2.5, 'Smooth ride in morning traffic.'),
(2, 2, 'Hamilton', 'Toronto', '2024-09-15 12:00:00', '2024-09-15 13:30:00', 70.20, 5.0, NULL),
(3, 1, 'Ottawa', 'Montreal', '2024-08-20 06:30:00', '2024-08-20 09:45:00', 190.75, 20.0, 'Long haul delivery.'),
(4, 2, 'Waterloo', 'Guelph', '2024-10-12 15:00:00', '2024-10-12 15:45:00', 40.00, 1.8, 'Quick weekend ride.'),
(5, 1, 'Toronto', 'Brampton', '2024-07-11 08:30:00', '2024-07-11 09:00:00', 25.00, 0.0, 'Tesla trip, no fuel.'),
(6, 1, 'Barrie', 'Toronto', '2024-08-01 17:30:00', '2024-08-01 19:00:00', 90.00, 8.5, 'Heavy traffic during rush hour.'),
(7, 2, 'Kingston', 'Ottawa', '2024-09-05 07:00:00', '2024-09-05 09:30:00', 130.00, 7.2, NULL),
(8, 2, 'Windsor', 'London', '2024-08-22 14:00:00', '2024-08-22 16:30:00', 190.00, 11.0, 'Smooth ride on the highway.'),
(9, 1, 'Toronto', 'Niagara Falls', '2024-09-20 08:00:00', '2024-09-20 10:00:00', 130.00, 6.8, 'Tourist trip.'),
(10, 2, 'Thunder Bay', 'Sault Ste. Marie', '2024-09-14 06:00:00', '2024-09-14 12:00:00', 440.00, 22.0, 'Long-distance delivery.'),
(1, 1, 'Mississauga', 'Hamilton', '2024-07-22 10:30:00', '2024-07-22 11:45:00', 50.50, 4.0, NULL),
(2, 2, 'Toronto', 'Markham', '2024-09-17 18:00:00', '2024-09-17 18:45:00', 25.00, 1.2, 'Evening commute.'),
(3, 1, 'Ottawa', 'Kingston', '2024-10-02 05:30:00', '2024-10-02 07:15:00', 140.00, 12.5, 'Morning delivery.'),
(4, 2, 'Guelph', 'Kitchener', '2024-08-18 10:15:00', '2024-08-18 10:45:00', 20.00, 1.0, 'Quick ride for a meeting.'),
(5, 1, 'Brampton', 'Oakville', '2024-07-30 12:00:00', '2024-07-30 13:00:00', 45.00, 0.0, NULL),
(6, 2, 'Niagara Falls', 'Toronto', '2024-08-07 15:00:00', '2024-08-07 17:30:00', 125.00, 7.8, 'Highway trip with some traffic.'),
(7, 1, 'Ottawa', 'Toronto', '2024-09-25 05:00:00', '2024-09-25 12:00:00', 450.00, 30.0, 'Long trip with several stops.'),
(8, 2, 'London', 'Toronto', '2024-10-13 09:00:00', '2024-10-13 12:00:00', 200.00, 9.5, NULL),
(9, 1, 'Markham', 'Toronto', '2024-10-10 07:30:00', '2024-10-10 08:15:00', 30.00, 2.0, 'Smooth commute.'),
(10, 2, 'Sarnia', 'Windsor', '2024-09-06 13:00:00', '2024-09-06 15:00:00', 100.00, 6.5, 'Day trip.'),
(1, 2, 'Toronto', 'Mississauga', '2024-09-01 07:00:00', '2024-09-01 07:45:00', 30.00, 2.0, NULL),
(3, 1, 'Toronto', 'Montreal', '2024-07-01 04:00:00', '2024-07-01 10:00:00', 540.00, 50.0, 'Heavy cargo delivery.'),
(7, 2, 'Kingston', 'Toronto', '2024-10-05 06:30:00', '2024-10-05 10:15:00', 270.00, 15.0, 'Morning drive with light traffic.'),
(5, 1, 'Toronto', 'Guelph', '2024-08-25 15:00:00', '2024-08-25 17:00:00', 100.00, 0.0, 'Electric vehicle trip.'),
(9, 2, 'Thunder Bay', 'Sudbury', '2024-09-09 07:00:00', '2024-09-09 12:30:00', 500.00, 20.0, 'Long-distance business trip.'),
(6, 2, 'Burlington', 'Toronto', '2024-08-15 11:30:00', '2024-08-15 12:30:00', 55.00, 3.5, 'Smooth ride.'),
(8, 1, 'London', 'Guelph', '2024-07-17 09:00:00', '2024-07-17 10:30:00', 90.00, 5.0, 'Mid-morning trip.'),
(4, 1, 'Kitchener', 'Waterloo', '2024-07-10 14:00:00', '2024-07-10 14:15:00', 5.00, 0.3, 'Short ride.'),
(10, 2, 'Sault Ste. Marie', 'Thunder Bay', '2024-09-15 05:30:00', '2024-09-15 11:30:00', 400.00, 18.5, 'Long-distance delivery.'),
(2, 1, 'Toronto', 'Ottawa', '2024-10-14 07:00:00', '2024-10-14 12:30:00', 450.00, 20.0, 'Comfortable ride with Tesla.');

-- Ongoing rides with NULL end_location and end_date
INSERT INTO tb_ride_history 
(vehicle_id, user_id, start_location, start_date)
VALUES
(1, 1, 'Toronto', '2024-10-15 08:00:00'),
(2, 2, 'Mississauga', '2024-10-15 09:00:00'),
(3, 1, 'Ottawa', '2024-10-14 15:00:00'),
(4, 2, 'Waterloo', '2024-10-15 10:00:00'),
(5, 1, 'Hamilton', '2024-10-14 14:00:00'),
(6, 1, 'Toronto', '2024-10-15 07:30:00'),
(7, 2, 'Kingston', '2024-10-15 11:00:00'),
(8, 1, 'Guelph', '2024-10-14 12:00:00'),
(9, 2, 'Niagara Falls', '2024-10-15 06:00:00'),
(10, 1, 'Thunder Bay', '2024-10-15 09:30:00');

-- Insert some dummy fuel log
INSERT INTO tb_fuel_log 
(vehicle_id, user_id, fuel_date, liters_filled, fuel_price_per_liter, total_cost, comments)
VALUES
(1, 1, '2024-10-09 18:00:00', 40.00, 1.65, 66.00, 'Filled up before the morning trip to Mississauga.'),
(2, 2, '2024-09-15 11:45:00', 50.00, 1.60, 80.00, 'Topped off before heading to Toronto.'),
(3, 1, '2024-08-19 20:00:00', 100.00, 1.55, 155.00, 'Prepped for a long-distance delivery to Montreal.'),
(4, 2, '2024-10-12 14:30:00', 15.00, 1.70, 25.50, 'Quick fill-up for a short ride.'),
(6, 1, '2024-07-31 09:00:00', 60.00, 1.63, 97.80, 'Filled up after returning from Toronto.'),
(7, 2, '2024-09-05 17:00:00', 70.00, 1.58, 110.60, 'After arriving from Kingston.'),
(8, 2, '2024-08-22 17:00:00', 80.00, 1.55, 124.00, 'Post-trip fill-up in London.'),
(9, 1, '2024-09-19 19:30:00', 50.00, 1.67, 83.50, 'After visiting Niagara Falls.'),
(10, 2, '2024-09-14 13:00:00', 120.00, 1.53, 183.60, 'Filled up before the long delivery to Thunder Bay.'),
(1, 1, '2024-07-22 09:00:00', 30.00, 1.65, 49.50, 'Filled up in Hamilton before heading back to Toronto.'),
(3, 1, '2024-07-02 15:00:00', 80.00, 1.58, 126.40, 'Heavy cargo trip refueling.'),
(6, 2, '2024-08-07 18:00:00', 50.00, 1.62, 81.00, 'Topped off after heavy traffic drive.'),
(7, 1, '2024-09-25 18:00:00', 120.00, 1.54, 184.80, 'Filled up after long drive from Ottawa.'),
(8, 2, '2024-10-13 14:30:00', 90.00, 1.59, 143.10, 'Refueled after arriving from London.'),
(9, 1, '2024-10-10 18:00:00', 40.00, 1.66, 66.40, 'Before the commute to Toronto.'),
(10, 2, '2024-09-16 07:00:00', 100.00, 1.57, 157.00, 'Refueled before long drive to Sault Ste. Marie.'),
(4, 2, '2024-08-18 16:00:00', 10.00, 1.70, 17.00, 'Filled up for a local trip.'),
(6, 2, '2024-08-16 12:00:00', 60.00, 1.62, 97.20, 'After arriving in Toronto.'),
(7, 1, '2024-09-06 11:30:00', 100.00, 1.55, 155.00, 'Post-trip refill in Kingston.'),
(2, 1, '2024-10-14 18:00:00', 90.00, 1.60, 144.00, 'Long trip refuel in Ottawa.');

SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS ;