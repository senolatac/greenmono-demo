-- Add recipe columns to daily_meal_plans table for Recipe-based meal planning
ALTER TABLE daily_meal_plans ADD COLUMN breakfast_recipe_id BIGINT;
ALTER TABLE daily_meal_plans ADD COLUMN lunch_recipe_id BIGINT;
ALTER TABLE daily_meal_plans ADD COLUMN dinner_recipe_id BIGINT;

-- Add foreign key constraints
ALTER TABLE daily_meal_plans ADD CONSTRAINT fk_breakfast_recipe
    FOREIGN KEY (breakfast_recipe_id) REFERENCES recipes(id) ON DELETE SET NULL;

ALTER TABLE daily_meal_plans ADD CONSTRAINT fk_lunch_recipe
    FOREIGN KEY (lunch_recipe_id) REFERENCES recipes(id) ON DELETE SET NULL;

ALTER TABLE daily_meal_plans ADD CONSTRAINT fk_dinner_recipe
    FOREIGN KEY (dinner_recipe_id) REFERENCES recipes(id) ON DELETE SET NULL;

-- Add indexes for better query performance
CREATE INDEX idx_daily_meal_plan_breakfast_recipe ON daily_meal_plans(breakfast_recipe_id);
CREATE INDEX idx_daily_meal_plan_lunch_recipe ON daily_meal_plans(lunch_recipe_id);
CREATE INDEX idx_daily_meal_plan_dinner_recipe ON daily_meal_plans(dinner_recipe_id);

-- Add comments
COMMENT ON COLUMN daily_meal_plans.breakfast_recipe_id IS 'Recipe ID for breakfast meal';
COMMENT ON COLUMN daily_meal_plans.lunch_recipe_id IS 'Recipe ID for lunch meal';
COMMENT ON COLUMN daily_meal_plans.dinner_recipe_id IS 'Recipe ID for dinner meal';
