-- Insert additional soup and side dish recipes for 3-component lunch menu

-- Soup recipes (4 new)
INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Ezogelin Çorbası', 'Kırmızı mercimek, bulgur ve çeşitli baharatlarla hazırlanan geleneksel Türk çorbası.', 'SOUP', 40, 4, 200.00, 10.00, 32.00, 4.00, 6.00, true);

INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Domates Çorbası', 'Taze domateslerle hazırlanan kremsi ve lezzetli bir çorba.', 'SOUP', 30, 4, 150.00, 5.00, 22.00, 5.00, 4.00, true);

INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Tarhana Çorbası', 'Kurutulmuş tarhana ile hazırlanan geleneksel Anadolu çorbası.', 'SOUP', 25, 4, 170.00, 7.00, 28.00, 3.50, 3.00, true);

INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Yayla Çorbası', 'Yoğurt ve pirinçle hazırlanan hafif ve besleyici bir çorba.', 'SOUP', 35, 4, 160.00, 8.00, 20.00, 5.50, 1.50, true);

-- Side dish recipes (4 new)
INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Pilav', 'Tereyağı ve şehriye ile hazırlanan klasik Türk pilavı.', 'SIDE_DISH', 30, 4, 250.00, 5.00, 45.00, 6.00, 1.00, true);

INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Bulgur Pilavı', 'Domatesli ve biberli bulgur pilavı. Lif açısından zengin.', 'SIDE_DISH', 25, 4, 220.00, 7.00, 40.00, 4.00, 8.00, true);

INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Makarna Salatası', 'Renkli sebzeler ve zeytinyağı ile hazırlanan soğuk makarna salatası.', 'SIDE_DISH', 20, 4, 230.00, 6.00, 38.00, 7.00, 3.00, true);

INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Havuç Tarator', 'Rendelenmiş havuç, yoğurt ve sarımsakla hazırlanan ferahlatıcı meze.', 'SIDE_DISH', 15, 4, 120.00, 4.00, 15.00, 5.00, 3.50, true);

-- Update Tereyagli Makarna from MAIN_COURSE to SIDE_DISH
UPDATE recipes SET category = 'SIDE_DISH' WHERE name = 'Tereyağlı Makarna' AND category = 'MAIN_COURSE';
