CREATE TABLE IF NOT EXISTS feedback (
  id         BIGSERIAL PRIMARY KEY,
  job_id     BIGINT     NOT NULL UNIQUE REFERENCES jobs(id) ON DELETE CASCADE,
  rating     INT        NOT NULL,
  comment    TEXT,
  sentiment  VARCHAR(16),
  created_at TIMESTAMP  NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP  NOT NULL DEFAULT NOW(),
  CONSTRAINT chk_rating_range CHECK (rating BETWEEN 1 AND 5),
  CONSTRAINT chk_sentiment_allowed CHECK (sentiment IS NULL OR sentiment IN ('positive','negative','neutral'))
);

CREATE INDEX IF NOT EXISTS idx_feedback_rating ON feedback(rating);

-- updated_at trigger
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_trigger WHERE tgname = 'trg_feedback_set_updated_at'
  ) THEN
    CREATE TRIGGER trg_feedback_set_updated_at
      BEFORE UPDATE ON feedback
      FOR EACH ROW
      EXECUTE FUNCTION set_updated_at();
  END IF;
END$$;
