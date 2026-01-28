-- Create menu_plans table
CREATE TABLE menu_plans (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    user_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    total_calories INTEGER,
    average_daily_calories INTEGER,
    notes VARCHAR(2000),
    is_balanced BOOLEAN NOT NULL DEFAULT FALSE,
    balance_score DOUBLE PRECISION,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for menu_plans table
CREATE INDEX idx_menu_plan_user ON menu_plans(user_id);
CREATE INDEX idx_menu_plan_start_date ON menu_plans(start_date);
CREATE INDEX idx_menu_plan_status ON menu_plans(status);

-- Create menu_plan_meals junction table
CREATE TABLE menu_plan_meals (
    menu_plan_id BIGINT NOT NULL,
    meal_id BIGINT NOT NULL,
    PRIMARY KEY (menu_plan_id, meal_id),
    FOREIGN KEY (menu_plan_id) REFERENCES menu_plans(id) ON DELETE CASCADE,
    FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE
);

CREATE INDEX idx_menu_plan_meal_plan ON menu_plan_meals(menu_plan_id);
CREATE INDEX idx_menu_plan_meal_meal ON menu_plan_meals(meal_id);

-- Create daily_meal_plans table
CREATE TABLE daily_meal_plans (
    id BIGSERIAL PRIMARY KEY,
    menu_plan_id BIGINT NOT NULL,
    day_number INTEGER NOT NULL,
    meal_date DATE NOT NULL,
    breakfast_meal_id BIGINT,
    lunch_meal_id BIGINT,
    dinner_meal_id BIGINT,
    snack_meal_id BIGINT,
    total_calories INTEGER,
    notes VARCHAR(1000),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (menu_plan_id) REFERENCES menu_plans(id) ON DELETE CASCADE,
    FOREIGN KEY (breakfast_meal_id) REFERENCES meals(id) ON DELETE SET NULL,
    FOREIGN KEY (lunch_meal_id) REFERENCES meals(id) ON DELETE SET NULL,
    FOREIGN KEY (dinner_meal_id) REFERENCES meals(id) ON DELETE SET NULL,
    FOREIGN KEY (snack_meal_id) REFERENCES meals(id) ON DELETE SET NULL,
    UNIQUE (menu_plan_id, day_number),
    UNIQUE (menu_plan_id, meal_date)
);

-- Create indexes for daily_meal_plans table
CREATE INDEX idx_daily_meal_plan_menu ON daily_meal_plans(menu_plan_id);
CREATE INDEX idx_daily_meal_plan_date ON daily_meal_plans(meal_date);
CREATE INDEX idx_daily_meal_plan_day ON daily_meal_plans(day_number);

-- Add comments to menu_plans table
COMMENT ON TABLE menu_plans IS 'Stores weekly meal plans for users';
COMMENT ON COLUMN menu_plans.id IS 'Primary key';
COMMENT ON COLUMN menu_plans.name IS 'Name of the menu plan';
COMMENT ON COLUMN menu_plans.user_id IS 'ID of the user who owns this plan';
COMMENT ON COLUMN menu_plans.start_date IS 'Start date of the menu plan';
COMMENT ON COLUMN menu_plans.end_date IS 'End date of the menu plan';
COMMENT ON COLUMN menu_plans.status IS 'Status: DRAFT, ACTIVE, COMPLETED, ARCHIVED';
COMMENT ON COLUMN menu_plans.is_balanced IS 'Whether the menu plan is nutritionally balanced';
COMMENT ON COLUMN menu_plans.balance_score IS 'Nutritional balance score (0-100)';

COMMENT ON TABLE daily_meal_plans IS 'Stores daily meal assignments within a menu plan';
COMMENT ON COLUMN daily_meal_plans.day_number IS 'Day number within the menu plan (1-5 for 5-day plan)';
COMMENT ON COLUMN daily_meal_plans.meal_date IS 'Actual date for this daily plan';
