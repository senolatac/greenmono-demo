-- Create nutritional_info table
CREATE TABLE nutritional_info (
    id BIGSERIAL PRIMARY KEY,
    ingredient_id BIGINT UNIQUE,
    meal_id BIGINT UNIQUE,
    serving_size DECIMAL(10, 2) NOT NULL,
    serving_unit VARCHAR(20) NOT NULL,
    calories DECIMAL(10, 2) NOT NULL,
    protein DECIMAL(10, 2) NOT NULL,
    carbohydrates DECIMAL(10, 2) NOT NULL,
    fat DECIMAL(10, 2) NOT NULL,
    fiber DECIMAL(10, 2),
    sugar DECIMAL(10, 2),
    sodium DECIMAL(10, 2),
    cholesterol DECIMAL(10, 2),
    saturated_fat DECIMAL(10, 2),
    trans_fat DECIMAL(10, 2),
    vitamin_a DECIMAL(10, 2),
    vitamin_c DECIMAL(10, 2),
    vitamin_d DECIMAL(10, 2),
    calcium DECIMAL(10, 2),
    iron DECIMAL(10, 2),
    potassium DECIMAL(10, 2),
    notes VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE,
    FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE,
    CONSTRAINT chk_nutritional_entity CHECK (
        (ingredient_id IS NOT NULL AND meal_id IS NULL) OR
        (ingredient_id IS NULL AND meal_id IS NOT NULL)
    )
);

-- Create indexes for nutritional_info table
CREATE INDEX idx_nutritional_ingredient ON nutritional_info(ingredient_id);
CREATE INDEX idx_nutritional_meal ON nutritional_info(meal_id);
CREATE INDEX idx_nutritional_calories ON nutritional_info(calories);

-- Add comments to nutritional_info table
COMMENT ON TABLE nutritional_info IS 'Stores nutritional information for ingredients and meals';
COMMENT ON COLUMN nutritional_info.id IS 'Primary key';
COMMENT ON COLUMN nutritional_info.ingredient_id IS 'Reference to ingredient (mutually exclusive with meal_id)';
COMMENT ON COLUMN nutritional_info.meal_id IS 'Reference to meal (mutually exclusive with ingredient_id)';
COMMENT ON COLUMN nutritional_info.serving_size IS 'Size of one serving';
COMMENT ON COLUMN nutritional_info.serving_unit IS 'Unit of measurement for serving';
COMMENT ON COLUMN nutritional_info.calories IS 'Calories per serving';
COMMENT ON COLUMN nutritional_info.protein IS 'Protein in grams per serving';
COMMENT ON COLUMN nutritional_info.carbohydrates IS 'Carbohydrates in grams per serving';
COMMENT ON COLUMN nutritional_info.fat IS 'Fat in grams per serving';
COMMENT ON COLUMN nutritional_info.fiber IS 'Dietary fiber in grams per serving';
COMMENT ON COLUMN nutritional_info.sugar IS 'Sugar in grams per serving';
COMMENT ON COLUMN nutritional_info.sodium IS 'Sodium in milligrams per serving';
