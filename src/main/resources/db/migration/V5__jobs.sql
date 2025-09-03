DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'job_status') THEN
    CREATE TYPE job_status AS ENUM ('OPEN','OFFERED','ACCEPTED','IN_PROGRESS','COMPLETED','CANCELLED');
  END IF;
END$$;

CREATE TABLE IF NOT EXISTS jobs (
  id               BIGSERIAL PRIMARY KEY,
  customer_id      BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
  technician_id    BIGINT     REFERENCES users(id) ON DELETE SET NULL,
  category_id      BIGINT     REFERENCES service_categories(id) ON DELETE SET NULL,
  prompt           TEXT   NOT NULL,
  estimated_charge NUMERIC(10,2),
  distance_km      NUMERIC(8,2),
  status           job_status NOT NULL DEFAULT 'OPEN',
  created_at       TIMESTAMP  NOT NULL DEFAULT NOW(),
  updated_at       TIMESTAMP  NOT NULL DEFAULT NOW(),
  CONSTRAINT chk_estimated_charge_nonneg CHECK (estimated_charge IS NULL OR estimated_charge >= 0),
  CONSTRAINT chk_distance_nonneg        CHECK (distance_km IS NULL OR distance_km >= 0)
);

CREATE INDEX IF NOT EXISTS idx_jobs_customer   ON jobs(customer_id);
CREATE INDEX IF NOT EXISTS idx_jobs_technician ON jobs(technician_id);
CREATE INDEX IF NOT EXISTS idx_jobs_status     ON jobs(status);

-- updated_at trigger
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_trigger WHERE tgname = 'trg_jobs_set_updated_at'
  ) THEN
    CREATE TRIGGER trg_jobs_set_updated_at
      BEFORE UPDATE ON jobs
      FOR EACH ROW
      EXECUTE FUNCTION set_updated_at();
  END IF;
END$$;
