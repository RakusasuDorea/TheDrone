-- Drones table
CREATE TABLE IF NOT EXISTS drones (
    id               BIGSERIAL    PRIMARY KEY,
    serial_number    VARCHAR(100) NOT NULL UNIQUE,
    model            VARCHAR(50)  NOT NULL,
    battery_capacity INT          NOT NULL,
    state            VARCHAR(20)  NOT NULL,
    created_at       TIMESTAMP,
    updated_at       TIMESTAMP
);

-- Medications table
CREATE TABLE IF NOT EXISTS medications (
    id         BIGSERIAL    PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    weight     INT          NOT NULL,
    code       VARCHAR(50)  NOT NULL UNIQUE,
    image      VARCHAR(255),
    drone_id   BIGINT REFERENCES drones (id),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Seed drones
INSERT INTO drones (serial_number, model, battery_capacity, state, created_at, updated_at)
VALUES
    ('DRONE001', 'Lightweight',   100, 'LOADING', NOW(), NOW()),
    ('DRONE002', 'Middleweight',   75, 'LOADING', NOW(), NOW()),
    ('DRONE003', 'Cruiserweight',  90, 'IDLE',    NOW(), NOW()),
    ('DRONE004', 'Heavyweight',    50, 'IDLE',    NOW(), NOW())
ON CONFLICT (serial_number) DO NOTHING;

-- Seed medications
INSERT INTO medications (name, weight, code, image, drone_id, created_at, updated_at)
SELECT 'Painkiller', 100, 'PAIN_K01', '/images/painkiller.jpg', d.id, NOW(), NOW()
FROM drones d WHERE d.serial_number = 'DRONE001'
ON CONFLICT (code) DO NOTHING;

INSERT INTO medications (name, weight, code, image, drone_id, created_at, updated_at)
SELECT 'Antibiotic', 200, 'ANTI_B01', '/images/antibiotic.jpg', d.id, NOW(), NOW()
FROM drones d WHERE d.serial_number = 'DRONE002'
ON CONFLICT (code) DO NOTHING;
