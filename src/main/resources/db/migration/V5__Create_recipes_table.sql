-- Create recipes table
CREATE TABLE recipes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(2000),
    category VARCHAR(50) NOT NULL,
    cooking_time_minutes INTEGER NOT NULL,
    servings INTEGER NOT NULL DEFAULT 1,
    calories DECIMAL(10, 2) NOT NULL,
    protein DECIMAL(10, 2) NOT NULL,
    carbohydrates DECIMAL(10, 2) NOT NULL,
    fat DECIMAL(10, 2),
    fiber DECIMAL(10, 2),
    image_url VARCHAR(500),
    user_id BIGINT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for recipes table
CREATE INDEX idx_recipe_name ON recipes(name);
CREATE INDEX idx_recipe_category ON recipes(category);
CREATE INDEX idx_recipe_user ON recipes(user_id);
CREATE INDEX idx_recipe_cooking_time ON recipes(cooking_time_minutes);
CREATE INDEX idx_recipe_active ON recipes(active);

-- Create recipe_ingredients table (many-to-many with quantity)
CREATE TABLE recipe_ingredients (
    id BIGSERIAL PRIMARY KEY,
    recipe_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    quantity DECIMAL(10, 2) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    notes VARCHAR(500),
    optional BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE
);

CREATE INDEX idx_recipe_ingredient_recipe ON recipe_ingredients(recipe_id);
CREATE INDEX idx_recipe_ingredient_ingredient ON recipe_ingredients(ingredient_id);

-- Create recipe_instructions table
CREATE TABLE recipe_instructions (
    recipe_id BIGINT NOT NULL,
    instruction VARCHAR(1000) NOT NULL,
    step_order INTEGER NOT NULL,
    FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);

CREATE INDEX idx_recipe_instructions_recipe ON recipe_instructions(recipe_id);

-- Add comments to recipes table
COMMENT ON TABLE recipes IS 'Stores recipe information with nutritional data';
COMMENT ON COLUMN recipes.id IS 'Primary key';
COMMENT ON COLUMN recipes.name IS 'Name of the recipe';
COMMENT ON COLUMN recipes.description IS 'Description of the recipe';
COMMENT ON COLUMN recipes.category IS 'Category: MAIN_COURSE, SOUP, APPETIZER, DESSERT, SIDE_DISH, SALAD, BREAKFAST, SNACK, BEVERAGE';
COMMENT ON COLUMN recipes.cooking_time_minutes IS 'Total cooking time in minutes';
COMMENT ON COLUMN recipes.servings IS 'Number of servings this recipe makes';
COMMENT ON COLUMN recipes.calories IS 'Total calories for entire recipe';
COMMENT ON COLUMN recipes.protein IS 'Protein content in grams for entire recipe';
COMMENT ON COLUMN recipes.carbohydrates IS 'Carbohydrate content in grams for entire recipe';
COMMENT ON COLUMN recipes.fat IS 'Fat content in grams for entire recipe';
COMMENT ON COLUMN recipes.fiber IS 'Fiber content in grams for entire recipe';
COMMENT ON COLUMN recipes.image_url IS 'URL to recipe image';
COMMENT ON COLUMN recipes.user_id IS 'ID of the user who created this recipe';
COMMENT ON COLUMN recipes.active IS 'Whether the recipe is active and available for use';

-- Add comments to recipe_ingredients table
COMMENT ON TABLE recipe_ingredients IS 'Junction table linking recipes with ingredients and quantities';
COMMENT ON COLUMN recipe_ingredients.id IS 'Primary key';
COMMENT ON COLUMN recipe_ingredients.recipe_id IS 'Foreign key to recipes table';
COMMENT ON COLUMN recipe_ingredients.ingredient_id IS 'Foreign key to ingredients table';
COMMENT ON COLUMN recipe_ingredients.quantity IS 'Quantity of ingredient needed';
COMMENT ON COLUMN recipe_ingredients.unit IS 'Unit of measurement: GRAM, KILOGRAM, MILLILITER, LITER, PIECE, TABLESPOON, TEASPOON, CUP, OUNCE, POUND';
COMMENT ON COLUMN recipe_ingredients.notes IS 'Additional notes about this ingredient in the recipe';
COMMENT ON COLUMN recipe_ingredients.optional IS 'Whether this ingredient is optional';
