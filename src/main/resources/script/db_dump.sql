USE fitness_app_db;

INSERT INTO user(id, name, username, password, email, contact_no, date_of_birth, gender, role, status)
VALUES (1, 'Khan Shafay', 'Khan', '$2a$12$aCyk/dRN15hL8iTIYD.fFuW277nNBExygEYtgRLAs4Wdo1NirOasO',
        'shafaykhan007@gmail.com', '9527687367', '1998-04-11', 'MALE', 'ADMIN',
        'ACTIVE'),
      (2, 'John Doe', 'User', '$2a$12$aCyk/dRN15hL8iTIYD.fFuW277nNBExygEYtgRLAs4Wdo1NirOasO',
        'johndoe@example.com', '9876543210', '1995-08-20', 'MALE', 'USER',
        'ACTIVE');

-- HEIGHT UNITS
INSERT INTO lookup (group_key, value)
VALUES ('HEIGHT_UOM', 'CM'),
       ('HEIGHT_UOM', 'INCH'),
       ('HEIGHT_UOM', 'FT'),
       ('WEIGHT_UOM', 'KG'),
       ('WEIGHT_UOM', 'GM'),
       ('WEIGHT_UOM', 'LB'),
       ('DURATION_UOM', 'MINUTE'),
       ('DURATION_UOM', 'HOUR'),
       ('WORKOUT_TYPE', 'CARDIO'),
       ('WORKOUT_TYPE', 'STRENGTH'),
       ('WORKOUT_TYPE', 'YOGA'),
       ('WORKOUT_INTENSITY', 'LOW'),
       ('WORKOUT_INTENSITY', 'MEDIUM'),
       ('WORKOUT_INTENSITY', 'HIGH'),
       ('GOAL_TYPE', 'WEIGHT LOSS'),
       ('GOAL_TYPE', 'MUSCLE GAIN'),
       ('MEAL_TYPE', 'BREAKFAST'),
       ('MEAL_TYPE', 'LUNCH'),
       ('MEAL_TYPE', 'DINNER'),
       ('MEAL_TYPE', 'SNACK'),
       ('DIET_TYPE', 'VEG'),
       ('DIET_TYPE', 'NON VEG'),
       ('DIET_TYPE', 'KETO');

-- WORKOUT
-- Note: IDs for foreign keys are inferred based on the sequential insertion order in the lookup table above.
-- 7=MINUTE, 8=HOUR, 9=CARDIO, 10=STRENGTH, 11=YOGA, 12=LOW, 13=MEDIUM, 14=HIGH
INSERT INTO workout (user_id, type_id, duration_uom_id, intensity_id, exercise_name, duration, calories_burned, notes, record_date_time, status, record_status)
VALUES (1, 9, 7, 13, 'Morning Jog', 30, 250, 'Light cardio session to start the day', '2023-11-01 07:30:00', 'ACTIVE', 1),
       (1, 10, 7, 14, 'Upper Body Power', 45, 320, 'Focus on chest and triceps', '2023-11-02 18:00:00', 'ACTIVE', 1),
       (3, 11, 8, 12, 'Restorative Yoga', 1, 120, 'Recovery session', '2023-11-03 06:00:00', 'ACTIVE', 1);

-- HEALTH RECORD
INSERT INTO health_record (user_id, weight, sleep_hours, water_intake, bmi, heart_rate, steps, record_date_time, status, record_status)
VALUES (1, 75.5, 7.5, 2.5, 24.2, 72, 8500, '2023-11-01 08:00:00', 'ACTIVE', 1),
       (1, 75.2, 6.0, 3.0, 24.1, 75, 10200, '2023-11-02 08:00:00', 'ACTIVE', 1),
       (2, 82.0, 8.0, 2.0, 26.5, 68, 6000, '2023-11-01 09:00:00', 'ACTIVE', 1);

-- GOAL
-- 15=WEIGHT LOSS, 16=MUSCLE GAIN
INSERT INTO goal (user_id, type_id, target_value, current_value, description, start_date, end_date, record_date_time, status, record_status)
VALUES (1, 15, 70.0, 75.5, 'Lose 5kg in 2 months', '2023-11-01', '2023-12-31', '2023-11-01 10:00:00', 'ACTIVE', 1),
       (2, 16, 85.0, 82.0, 'Gain muscle mass', '2023-11-01', '2024-01-31', '2023-11-01 10:00:00', 'ACTIVE', 1);

-- DIET RECORD
-- 17=BREAKFAST, 18=LUNCH, 19=DINNER, 20=SNACK
INSERT INTO diet_record (user_id, meal_type_id, food_name, calories, protein, carbs, fat, record_date_time, status, record_status)
VALUES (1, 17, 'Oatmeal with fruits', 350, 12.0, 60.0, 6.0, '2023-11-01 08:30:00', 'ACTIVE', 1),
       (1, 18, 'Grilled Chicken Salad', 450, 40.0, 15.0, 20.0, '2023-11-01 13:00:00', 'ACTIVE', 1),
       (2, 19, 'Salmon and Quinoa', 600, 35.0, 45.0, 25.0, '2023-11-01 20:00:00', 'ACTIVE', 1);

-- USER PREFERENCE
-- Diet: 21=VEG, 22=NON VEG, 23=KETO
-- Intensity: 12=LOW, 13=MEDIUM, 14=HIGH
-- Goal: 15=WEIGHT LOSS, 16=MUSCLE GAIN
INSERT INTO user_preference (user_id, diet_type_id, intensity_id, goal_type_id, allergies)
VALUES (1, 22, 13, 15, 'Peanuts'),
       (2, 21, 12, 16, 'None');
