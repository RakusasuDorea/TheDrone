-- Create `drone` table
CREATE TABLE IF NOT EXISTS drone (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     serial_number VARCHAR(100) NOT NULL,
                                     model VARCHAR(50) NOT NULL,
                                     weight_limit INT NOT NULL,
                                     battery_capacity INT NOT NULL,
                                     state VARCHAR(20) NOT NULL
);

-- Create `medication` table
CREATE TABLE IF NOT EXISTS medication (
                                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          name VARCHAR(100) NOT NULL,
                                          weight INT NOT NULL,
                                          code VARCHAR(50) NOT NULL,
                                          image VARCHAR(255),
                                          drone_id BIGINT,
                                          FOREIGN KEY (drone_id) REFERENCES drone(id)
);

INSERT INTO drone (serial_number, model, weight_limit, battery_capacity, state) VALUES
                                                                                    ('DRONE001', 'Lightweight', 250, 100, 'LOADING'),
                                                                                    ('DRONE002', 'Middleweight', 500, 75, 'LOADING'),
                                                                                    ('DRONE003', 'Cruiserweight', 750, 90,'IDLE');

INSERT INTO medication (name, weight, code, image, drone_id)
SELECT 'Painkiller', 100, 'PAIN_K01', '/images/painkiller.jpg', d.id
FROM drone d
WHERE d.serial_number = 'DRONE001';

INSERT INTO medication (name, weight, code, image, drone_id)
SELECT 'Antibiotic', 200, 'ANTI_B01', '/images/antibiotic.jpg', d.id
FROM drone d
WHERE d.serial_number = 'DRONE002';
