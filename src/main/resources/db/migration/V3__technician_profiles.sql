CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TABLE IF NOT EXISTS technician_profiles (
  id                 BIGSERIAL PRIMARY KEY,
  user_id            BIGINT       NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
  skills             TEXT         NOT NULL,
  base_rate          NUMERIC(10,2) NOT NULL DEFAULT 0,
  rating_avg         NUMERIC(3,2)  NOT NULL DEFAULT 0,
  jobs_completed     INT           NOT NULL DEFAULT 0,
  service_radius_km  NUMERIC(6,2)  NOT NULL DEFAULT 10,
  lat                NUMERIC(9,6),
  lng                NUMERIC(9,6),
  created_at         TIMESTAMP     NOT NULL DEFAULT NOW(),
  updated_at         TIMESTAMP     NOT NULL DEFAULT NOW(),
  CONSTRAINT chk_service_radius_positive CHECK (service_radius_km >= 0),
  CONSTRAINT chk_base_rate_nonneg CHECK (base_rate >= 0),
  CONSTRAINT chk_lat_range CHECK (lat IS NULL OR (lat BETWEEN -90 AND 90)),
  CONSTRAINT chk_lng_range CHECK (lng IS NULL OR (lng BETWEEN -180 AND 180))
);

CREATE INDEX IF NOT EXISTS idx_tech_profiles_user   ON technician_profiles(user_id);
CREATE INDEX IF NOT EXISTS idx_tech_profiles_rating ON technician_profiles(rating_avg);

-- updated_at trigger
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_trigger WHERE tgname = 'trg_tech_profiles_set_updated_at'
  ) THEN
    CREATE TRIGGER trg_tech_profiles_set_updated_at
      BEFORE UPDATE ON technician_profiles
      FOR EACH ROW
      EXECUTE FUNCTION set_updated_at();
  END IF;
END$$;
