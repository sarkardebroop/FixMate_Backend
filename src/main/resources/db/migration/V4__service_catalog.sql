-- Parent: service_categories
CREATE TABLE IF NOT EXISTS service_categories (
  id          BIGSERIAL PRIMARY KEY,
  name        VARCHAR(120) NOT NULL UNIQUE,
  description TEXT,
  created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
  updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Child: service_items (belongs to a category)
CREATE TABLE IF NOT EXISTS service_items (
  id           BIGSERIAL PRIMARY KEY,
  category_id  BIGINT       NOT NULL REFERENCES service_categories(id) ON DELETE CASCADE,
  title        VARCHAR(160) NOT NULL,
  base_price   NUMERIC(10,2) NOT NULL DEFAULT 0,
  active       BOOLEAN       NOT NULL DEFAULT TRUE,
  created_at   TIMESTAMP     NOT NULL DEFAULT NOW(),
  updated_at   TIMESTAMP     NOT NULL DEFAULT NOW(),
  CONSTRAINT chk_item_price_nonneg CHECK (base_price >= 0)
);

CREATE INDEX IF NOT EXISTS idx_items_category ON service_items(category_id);
CREATE INDEX IF NOT EXISTS idx_items_active   ON service_items(active);

-- updated_at triggers
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_trigger WHERE tgname = 'trg_categories_set_updated_at'
  ) THEN
    CREATE TRIGGER trg_categories_set_updated_at
      BEFORE UPDATE ON service_categories
      FOR EACH ROW
      EXECUTE FUNCTION set_updated_at();
  END IF;

  IF NOT EXISTS (
    SELECT 1 FROM pg_trigger WHERE tgname = 'trg_items_set_updated_at'
  ) THEN
    CREATE TRIGGER trg_items_set_updated_at
      BEFORE UPDATE ON service_items
      FOR EACH ROW
      EXECUTE FUNCTION set_updated_at();
  END IF;
END$$;
