-- Create meals table
CREATE TABLE meals (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(2000),
    meal_type VARCHAR(20) NOT NULL,
    prep_time_minutes INTEGER NOT NULL,
    cook_time_minutes INTEGER NOT NULL,
    servings INTEGER NOT NULL,
    difficulty_level VARCHAR(20) NOT NULL,
    image_url VARCHAR(500),
    user_id BIGINT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for meals table
CREATE INDEX idx_meal_name ON meals(name);
CREATE INDEX idx_meal_type ON meals(meal_type);
CREATE INDEX idx_meal_difficulty ON meals(difficulty_level);
CREATE INDEX idx_meal_user ON meals(user_id);

-- Create meal_ingredients junction table
CREATE TABLE meal_ingredients (
    meal_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    PRIMARY KEY (meal_id, ingredient_id),
    FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE
);

CREATE INDEX idx_meal_ingredient_meal ON meal_ingredients(meal_id);
CREATE INDEX idx_meal_ingredient_ingredient ON meal_ingredients(ingredient_id);

-- Create meal_instructions table
CREATE TABLE meal_instructions (
    meal_id BIGINT NOT NULL,
    instruction VARCHAR(1000) NOT NULL,
    step_order INTEGER NOT NULL,
    FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE
);

CREATE INDEX idx_meal_instructions_meal ON meal_instructions(meal_id);

-- Create meal_tags table
CREATE TABLE meal_tags (
    meal_id BIGINT NOT NULL,
    tag VARCHAR(50) NOT NULL,
    FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE
);

CREATE INDEX idx_meal_tags_meal ON meal_tags(meal_id);

-- Add comments to meals table
COMMENT ON TABLE meals IS 'Stores meal recipes with cooking instructions';
COMMENT ON COLUMN meals.id IS 'Primary key';
COMMENT ON COLUMN meals.name IS 'Name of the meal';
COMMENT ON COLUMN meals.description IS 'Description of the meal';
COMMENT ON COLUMN meals.meal_type IS 'Type: BREAKFAST, LUNCH, DINNER, SNACK, DESSERT';
COMMENT ON COLUMN meals.prep_time_minutes IS 'Preparation time in minutes';
COMMENT ON COLUMN meals.cook_time_minutes IS 'Cooking time in minutes';
COMMENT ON COLUMN meals.servings IS 'Number of servings';
COMMENT ON COLUMN meals.difficulty_level IS 'Difficulty: EASY, MEDIUM, HARD';
COMMENT ON COLUMN meals.active IS 'Whether the meal is active for menu planning';
