CREATE TABLE IF NOT EXISTS uav (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    frame_sn VARCHAR(64) NOT NULL UNIQUE,
    model_name VARCHAR(128) NOT NULL,
    manufacturer VARCHAR(128),
    status VARCHAR(32) NOT NULL DEFAULT 'IDLE',
    max_flight_altitude INTEGER,
    max_flight_speed INTEGER,
    flight_duration INTEGER,
    battery_capacity INTEGER,
    payload_capacity INTEGER,
    notes VARCHAR(512),
    ai_attributes TEXT,
    ai_generation_status VARCHAR(32) DEFAULT 'NONE',
    ai_last_error VARCHAR(512),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_uav_model ON uav(model_name);
CREATE INDEX IF NOT EXISTS idx_uav_manufacturer ON uav(manufacturer);
CREATE INDEX IF NOT EXISTS idx_uav_status ON uav(status);