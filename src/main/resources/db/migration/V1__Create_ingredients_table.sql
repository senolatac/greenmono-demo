-- Create ingredients table
CREATE TABLE ingredients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    quantity DECIMAL(10, 2) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    expiry_date DATE,
    notes VARCHAR(500),
    available BOOLEAN NOT NULL DEFAULT TRUE,
    user_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for ingredients table
CREATE INDEX idx_ingredient_name ON ingredients(name);
CREATE INDEX idx_ingredient_category ON ingredients(category);
CREATE INDEX idx_ingredient_expiry ON ingredients(expiry_date);
CREATE INDEX idx_ingredient_user ON ingredients(user_id);

-- Add comments to ingredients table
COMMENT ON TABLE ingredients IS 'Stores user ingredients available for meal planning';
COMMENT ON COLUMN ingredients.id IS 'Primary key';
COMMENT ON COLUMN ingredients.name IS 'Name of the ingredient';
COMMENT ON COLUMN ingredients.category IS 'Category: VEGETABLES, FRUITS, MEAT, POULTRY, FISH, SEAFOOD, DAIRY, GRAINS, LEGUMES, NUTS_SEEDS, HERBS_SPICES, OILS_FATS, CONDIMENTS, BEVERAGES, OTHER';
COMMENT ON COLUMN ingredients.quantity IS 'Quantity available';
COMMENT ON COLUMN ingredients.unit IS 'Unit of measurement: GRAM, KILOGRAM, MILLILITER, LITER, PIECE, TABLESPOON, TEASPOON, CUP, OUNCE, POUND';
COMMENT ON COLUMN ingredients.expiry_date IS 'Expiration date of the ingredient';
COMMENT ON COLUMN ingredients.notes IS 'Additional notes about the ingredient';
COMMENT ON COLUMN ingredients.available IS 'Whether the ingredient is currently available';
COMMENT ON COLUMN ingredients.user_id IS 'ID of the user who owns this ingredient';
