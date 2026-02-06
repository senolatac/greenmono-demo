-- Set user_id=1 for all recipes that don't have a user assigned
UPDATE recipes SET user_id = 1 WHERE user_id IS NULL;

-- Set user_id=1 for all ingredients that don't have a user assigned
UPDATE ingredients SET user_id = 1 WHERE user_id IS NULL;
