-- Insert nutritional information for common ingredients
-- Values are per 100g serving size unless otherwise noted

-- VEGETABLES
INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 25, 2.9, 3.6, 0.4, 2.6, 1.7, 33, 0, 0.1, 0, 469, 51, 0, 47, 0.9, 320, 'Tomato - per 100g'
FROM ingredients WHERE name = 'Domates' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 23, 1.0, 4.6, 0.2, 2.0, 2.3, 6, 0, 0, 0, 15, 14, 0, 16, 0.3, 147, 'Onion - per 100g'
FROM ingredients WHERE name = 'Soğan' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 31, 1.8, 6.0, 0.2, 2.8, 2.3, 5, 0, 0, 0, 241, 80, 0, 24, 0.5, 320, 'Pepper - per 100g'
FROM ingredients WHERE name = 'Biber' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 34, 3.4, 6.6, 0.2, 2.2, 1.4, 18, 0, 0.1, 0, 500, 120, 0, 30, 1.1, 290, 'Spinach - per 100g'
FROM ingredients WHERE name = 'Ispanak' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 41, 1.8, 8.8, 0.2, 2.8, 4.7, 16, 0, 0, 0, 835, 5.1, 0, 30, 0.5, 320, 'Carrot - per 100g'
FROM ingredients WHERE name = 'Havuç' AND ingredient_id IS NULL LIMIT 1;

-- MEAT & POULTRY
INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 250, 26, 0, 15, 0, 0, 72, 88, 6, 0.5, 0, 0, 0.2, 18, 2.6, 302, 'Chicken breast - per 100g'
FROM ingredients WHERE name = 'Tavuk Göğsü' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 143, 20.5, 0, 6.5, 0, 0, 80, 62, 2.3, 0.3, 0, 0, 0, 14, 1.0, 192, 'Chicken thigh - per 100g'
FROM ingredients WHERE name = 'Tavuk But' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 250, 26, 0, 17, 0, 0, 70, 90, 7, 0.5, 0, 0, 0, 12, 2.5, 350, 'Beef - per 100g'
FROM ingredients WHERE name = 'Dana Eti' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 294, 25, 0, 21, 0, 0, 72, 86, 9, 0.8, 0, 0, 0, 17, 1.9, 318, 'Ground beef - per 100g'
FROM ingredients WHERE name = 'Kıyma' AND ingredient_id IS NULL LIMIT 1;

-- DAIRY
INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 61, 3.2, 4.8, 3.3, 0, 5.1, 44, 13, 2.1, 0.1, 46, 0, 0.5, 113, 0, 143, 'Milk - per 100ml'
FROM ingredients WHERE name = 'Süt' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 59, 10, 3.6, 0.4, 0, 3.2, 36, 5, 0.3, 0, 27, 0, 0, 110, 0.1, 141, 'Yogurt - per 100g'
FROM ingredients WHERE name = 'Yoğurt' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 264, 18, 1.3, 21, 0, 0.5, 621, 89, 13, 0.5, 220, 0, 0.5, 721, 0.1, 98, 'White cheese - per 100g'
FROM ingredients WHERE name = 'Beyaz Peynir' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 717, 0.6, 0.1, 81, 0, 0.1, 11, 215, 51, 3.3, 684, 0, 1.5, 24, 0, 24, 'Butter - per 100g'
FROM ingredients WHERE name = 'Tereyağı' AND ingredient_id IS NULL LIMIT 1;

-- GRAINS & LEGUMES
INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 365, 7.1, 77, 0.9, 2.2, 0.1, 5, 0, 0.2, 0, 0, 0, 0, 28, 1.2, 115, 'Rice - uncooked per 100g'
FROM ingredients WHERE name = 'Pirinç' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 364, 10.3, 76, 1.0, 3.2, 0.4, 2, 0, 0.2, 0, 0, 0, 0, 15, 3.6, 107, 'Pasta - uncooked per 100g'
FROM ingredients WHERE name = 'Makarna' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 83, 3.4, 20, 0.3, 2.7, 0.4, 1, 0, 0.1, 0, 0, 0, 0, 3, 0.4, 54, 'Potato - per 100g'
FROM ingredients WHERE name = 'Patates' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 341, 20, 63, 1.3, 15, 2.1, 6, 0, 0.1, 0, 6, 1.5, 0, 105, 6.7, 1393, 'Lentils - dry per 100g'
FROM ingredients WHERE name = 'Mercimek' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 347, 19, 63, 1.2, 15, 2.0, 12, 0, 0.1, 0, 3, 0.5, 0, 83, 4.7, 1240, 'Chickpeas - dry per 100g'
FROM ingredients WHERE name = 'Nohut' AND ingredient_id IS NULL LIMIT 1;

-- OILS & FATS
INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 15, 'MILLILITER', 884, 0, 0, 100, 0, 0, 2, 0, 14, 0, 0, 0, 0, 0, 0.6, 1, 'Olive oil - per 100ml'
FROM ingredients WHERE name = 'Zeytinyağı' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 15, 'MILLILITER', 884, 0, 0, 100, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0, 0, 'Sunflower oil - per 100ml'
FROM ingredients WHERE name = 'Ayçiçek Yağı' AND ingredient_id IS NULL LIMIT 1;

-- EGGS
INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 1, 'PIECE', 155, 13, 1.1, 11, 0, 0.6, 124, 373, 3.3, 0, 160, 0, 2, 56, 1.8, 138, 'Egg - per large egg (50g)'
FROM ingredients WHERE name = 'Yumurta' AND ingredient_id IS NULL LIMIT 1;

-- FRUITS
INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 52, 0.3, 14, 0.2, 2.4, 10, 1, 0, 0, 0, 3, 4.6, 0, 6, 0.1, 107, 'Apple - per 100g'
FROM ingredients WHERE name = 'Elma' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 89, 1.1, 23, 0.3, 2.6, 12, 1, 0, 0, 0, 64, 9, 0, 5, 0.3, 358, 'Banana - per 100g'
FROM ingredients WHERE name = 'Muz' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 47, 0.9, 12, 0.1, 2.0, 9.4, 2, 0, 0, 0, 225, 53, 0, 13, 0.3, 181, 'Orange - per 100g'
FROM ingredients WHERE name = 'Portakal' AND ingredient_id IS NULL LIMIT 1;

-- HERBS & SPICES
INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 5, 'GRAM', 149, 3.0, 37, 0.5, 11, 0.9, 25, 0, 0.1, 0, 27, 5, 0, 138, 8.1, 431, 'Garlic - per 5g clove'
FROM ingredients WHERE name = 'Sarımsak' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 5, 'GRAM', 247, 14, 63, 1.2, 25, 2.3, 18, 0, 0.3, 0, 292, 2.1, 0, 1002, 17, 1724, 'Cumin - per 100g'
FROM ingredients WHERE name = 'Kimyon' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 2, 'GRAM', 0, 0.1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 4, 0, 11, 'Salt - per 2g'
FROM ingredients WHERE name = 'Tuz' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 2, 'GRAM', 251, 3.5, 65, 3.3, 26, 0.5, 26, 0, 0.2, 0, 26, 21, 0, 40, 26, 1267, 'Black pepper - per 100g'
FROM ingredients WHERE name = 'Karabiber' AND ingredient_id IS NULL LIMIT 1;

-- FISH & SEAFOOD
INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 206, 22, 0, 13, 0, 0, 50, 63, 3, 0, 40, 0, 11, 12, 0.8, 363, 'Salmon - per 100g'
FROM ingredients WHERE name = 'Somon' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 100, 'GRAM', 96, 20, 0, 1.7, 0, 0, 74, 63, 0.4, 0, 8, 0, 1.2, 13, 0.3, 417, 'Sea bass - per 100g'
FROM ingredients WHERE name = 'Levrek' AND ingredient_id IS NULL LIMIT 1;

-- BREAD
INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 30, 'GRAM', 265, 9, 49, 3.2, 2.7, 5, 491, 0, 0.5, 0, 0, 0, 0, 153, 3.6, 115, 'White bread - per slice (30g)'
FROM ingredients WHERE name = 'Ekmek' AND ingredient_id IS NULL LIMIT 1;

INSERT INTO nutritional_info (ingredient_id, serving_size, serving_unit, calories, protein, carbohydrates, fat, fiber, sugar, sodium, cholesterol, saturated_fat, trans_fat, vitamin_a, vitamin_c, vitamin_d, calcium, iron, potassium, notes)
SELECT id, 30, 'GRAM', 247, 13, 41, 3.4, 7, 6, 432, 0, 0.6, 0, 0, 0, 0, 54, 2.5, 248, 'Whole wheat bread - per slice (30g)'
FROM ingredients WHERE name = 'Kepekli Ekmek' AND ingredient_id IS NULL LIMIT 1;

COMMIT;
