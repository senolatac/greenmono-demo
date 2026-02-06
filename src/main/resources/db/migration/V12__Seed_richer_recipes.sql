-- Seed a richer Turkish dataset (DEV/DEMO only).
-- WARNING: This migration is destructive and clears existing menu/recipe data.

TRUNCATE TABLE
  recipe_instructions,
  recipe_ingredients,
  daily_meal_plans,
  menu_plan_meals,
  menu_plans,
  recipes,
  ingredients
RESTART IDENTITY
CASCADE;

-- Ingredients (sample pantry)
INSERT INTO ingredients (name, category, quantity, unit, available, user_id) VALUES
('Un', 'GRAINS', 2000, 'GRAM', true, 1),
('Yumurta', 'DAIRY', 20, 'PIECE', true, 1),
('Süt', 'DAIRY', 2000, 'MILLILITER', true, 1),
('Yoğurt', 'DAIRY', 2000, 'GRAM', true, 1),
('Tereyağı', 'DAIRY', 500, 'GRAM', true, 1),
('Zeytinyağı', 'OILS_FATS', 1000, 'MILLILITER', true, 1),
('Tavuk Göğsü', 'POULTRY', 1200, 'GRAM', true, 1),
('Kuzu Eti', 'MEAT', 800, 'GRAM', true, 1),
('Dana Kıyma', 'MEAT', 1000, 'GRAM', true, 1),
('Somon Fileto', 'FISH', 600, 'GRAM', true, 1),
('Pirinç', 'GRAINS', 1000, 'GRAM', true, 1),
('Bulgur', 'GRAINS', 1000, 'GRAM', true, 1),
('Makarna', 'GRAINS', 1000, 'GRAM', true, 1),
('Nohut', 'LEGUMES', 1000, 'GRAM', true, 1),
('Kuru Fasulye', 'LEGUMES', 1000, 'GRAM', true, 1),
('Patates', 'VEGETABLES', 3000, 'GRAM', true, 1),
('Soğan', 'VEGETABLES', 2000, 'GRAM', true, 1),
('Domates', 'VEGETABLES', 2000, 'GRAM', true, 1),
('Biber', 'VEGETABLES', 1000, 'GRAM', true, 1),
('Patlıcan', 'VEGETABLES', 2000, 'GRAM', true, 1),
('Havuç', 'VEGETABLES', 1000, 'GRAM', true, 1),
('Brokoli', 'VEGETABLES', 2, 'PIECE', true, 1),
('Semizotu', 'VEGETABLES', 1000, 'GRAM', true, 1),
('Ispanak', 'VEGETABLES', 1000, 'GRAM', true, 1),
('Lahana', 'VEGETABLES', 1, 'PIECE', true, 1),
('Sarımsak', 'HERBS_SPICES', 200, 'GRAM', true, 1),
('Tuz', 'HERBS_SPICES', 500, 'GRAM', true, 1),
('Karabiber', 'HERBS_SPICES', 100, 'GRAM', true, 1),
('Kırmızı Pul Biber', 'HERBS_SPICES', 100, 'GRAM', true, 1),
('Kimyon', 'HERBS_SPICES', 100, 'GRAM', true, 1),
('Limon', 'FRUITS', 10, 'PIECE', true, 1),
('Maydanoz', 'HERBS_SPICES', 2, 'PIECE', true, 1),
('Dereotu', 'HERBS_SPICES', 2, 'PIECE', true, 1),
('Kırmızı Mercimek', 'LEGUMES', 1000, 'GRAM', true, 1),
('Arpa Şehriye', 'GRAINS', 500, 'GRAM', true, 1),
('Tarhana', 'GRAINS', 500, 'GRAM', true, 1),
('İşkembe', 'MEAT', 600, 'GRAM', true, 1),
('Mantar', 'VEGETABLES', 600, 'GRAM', true, 1),
('Krema', 'DAIRY', 500, 'GRAM', true, 1),
('Taze Fasulye', 'VEGETABLES', 1000, 'GRAM', true, 1),
('Enginar', 'VEGETABLES', 4, 'PIECE', true, 1),
('Salatalık', 'VEGETABLES', 1000, 'GRAM', true, 1),
('Bezelye', 'VEGETABLES', 500, 'GRAM', true, 1),
('Barbunya', 'LEGUMES', 500, 'GRAM', true, 1);

-- Soups (12)
INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Mercimek Çorbası', 'Kırmızı mercimek, havuç ve soğanla hazırlanan klasik çorba.', 'SOUP', 35, 6, 420.00, 24.00, 60.00, 12.00, 18.00, true),
('Ezogelin Çorbası', 'Bulgur ve baharatlarla zenginleşen geleneksel çorba.', 'SOUP', 40, 6, 450.00, 22.00, 66.00, 14.00, 16.00, true),
('Tarhana Çorbası', 'Kurutulmuş tarhanayla yapılan besleyici çorba.', 'SOUP', 25, 4, 360.00, 14.00, 52.00, 10.00, 8.00, true),
('Yayla Çorbası', 'Yoğurt ve pirinçle yapılan hafif çorba.', 'SOUP', 30, 4, 320.00, 12.00, 40.00, 12.00, 4.00, true),
('Domates Çorbası', 'Taze domatesle hazırlanan kremsi çorba.', 'SOUP', 25, 4, 280.00, 8.00, 42.00, 9.00, 6.00, true),
('Tavuk Suyu Çorbası', 'Didiklenmiş tavuk ve sebzelerle hazırlanan şifa çorbası.', 'SOUP', 45, 6, 300.00, 28.00, 20.00, 8.00, 2.00, true),
('Şehriye Çorbası', 'Arpa şehriye ile pratik ve hafif çorba.', 'SOUP', 30, 4, 340.00, 10.00, 55.00, 8.00, 4.00, true),
('İşkembe Çorbası', 'Sarımsaklı ve sirkeli geleneksel çorba.', 'SOUP', 60, 4, 360.00, 32.00, 18.00, 16.00, 2.00, true),
('Kremalı Mantarlı Çorba', 'Mantar ve krema ile yapılan yoğun lezzet.', 'SOUP', 35, 4, 380.00, 12.00, 28.00, 22.00, 3.00, true),
('Sebze Çorbası', 'Mevsim sebzeleriyle hazırlanan hafif çorba.', 'SOUP', 30, 4, 240.00, 8.00, 34.00, 6.00, 7.00, true),
('Brokoli Çorbası', 'Brokoli ve patatesle hazırlanan yeşil çorba.', 'SOUP', 25, 4, 260.00, 12.00, 28.00, 8.00, 8.00, true),
('Düğün Çorbası', 'Yoğurt terbiyeli, etli geleneksel çorba.', 'SOUP', 45, 6, 480.00, 30.00, 50.00, 18.00, 6.00, true);

-- Main courses (20)
INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Karnıyarık', 'Fırında pişmiş patlıcanın kıymalı harçla buluşması.', 'MAIN_COURSE', 70, 4, 760.00, 36.00, 48.00, 42.00, 16.00, true),
('İmam Bayıldı', 'Zeytinyağlı, soğanlı harçla doldurulmuş patlıcan.', 'MAIN_COURSE', 75, 4, 640.00, 16.00, 58.00, 36.00, 18.00, true),
('Hünkar Beğendi', 'Beşamel patlıcan püresi üzerinde et sote.', 'MAIN_COURSE', 80, 4, 820.00, 46.00, 40.00, 48.00, 8.00, true),
('Kuzu İncik', 'Uzun sürede pişen kemikli kuzu eti.', 'MAIN_COURSE', 120, 4, 980.00, 70.00, 20.00, 62.00, 2.00, true),
('Etli Nohut', 'Nohut ve kuşbaşı etle yapılan ev yemeği.', 'MAIN_COURSE', 90, 4, 720.00, 42.00, 70.00, 24.00, 18.00, true),
('Etli Kuru Fasulye', 'Fasulye ve etle yapılan klasik tencere yemeği.', 'MAIN_COURSE', 90, 4, 760.00, 38.00, 90.00, 18.00, 24.00, true),
('Izgara Köfte', 'Baharatlı kıymadan hazırlanan köfte.', 'MAIN_COURSE', 35, 4, 860.00, 60.00, 20.00, 52.00, 4.00, true),
('Fırında Tavuk', 'Baharatlı tavuk parçalarıyla fırın lezzeti.', 'MAIN_COURSE', 55, 4, 640.00, 64.00, 12.00, 28.00, 2.00, true),
('Tas Kebabı', 'Dana eti ve sebzelerle yapılan tencere kebabı.', 'MAIN_COURSE', 80, 4, 780.00, 58.00, 35.00, 38.00, 6.00, true),
('Mantı', 'Kıyma dolgulu hamur ve yoğurt sosuyla servis.', 'MAIN_COURSE', 90, 4, 900.00, 40.00, 110.00, 30.00, 6.00, true),
('Perde Pilavı', 'Tavuklu pilavın yufka ile fırınlanması.', 'MAIN_COURSE', 85, 4, 840.00, 34.00, 110.00, 28.00, 6.00, true),
('Etli Taze Fasulye', 'Zeytinyağlı değil, etli ve domatesli fasulye.', 'MAIN_COURSE', 60, 4, 620.00, 36.00, 50.00, 26.00, 14.00, true),
('Patlıcan Musakka', 'Patlıcan ve kıymalı harçla katmanlı musakka.', 'MAIN_COURSE', 70, 4, 700.00, 32.00, 40.00, 38.00, 10.00, true),
('Tavuk Sote', 'Sebzelerle sotelenmiş tavuk parçaları.', 'MAIN_COURSE', 40, 4, 620.00, 58.00, 20.00, 22.00, 5.00, true),
('Çoban Kavurma', 'Kuşbaşı etin soğan ve biberle kavrulması.', 'MAIN_COURSE', 55, 4, 860.00, 62.00, 18.00, 54.00, 3.00, true),
('Fırında Somon', 'Limonlu ve otlu somon fileto.', 'MAIN_COURSE', 35, 4, 700.00, 60.00, 12.00, 40.00, 2.00, true),
('Zeytinyağlı Enginar', 'Sebzelerle doldurulmuş hafif zeytinyağlı.', 'MAIN_COURSE', 50, 4, 520.00, 12.00, 60.00, 24.00, 16.00, true),
('Etli Lahana Sarma', 'Etli harçla sarılmış lahana dolması.', 'MAIN_COURSE', 90, 4, 740.00, 34.00, 68.00, 34.00, 12.00, true),
('Kıymalı Ispanak', 'Ispanak ve kıymayla hazırlanan pratik ana yemek.', 'MAIN_COURSE', 35, 4, 560.00, 32.00, 28.00, 30.00, 10.00, true),
('Sebzeli Güveç', 'Fırında pişen mevsim sebzeleriyle güveç.', 'MAIN_COURSE', 60, 4, 580.00, 18.00, 70.00, 22.00, 14.00, true);

-- Side dishes (15)
INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Pilav', 'Tereyağı ve şehriye ile hazırlanan klasik pilav.', 'SIDE_DISH', 30, 4, 520.00, 10.00, 90.00, 12.00, 2.00, true),
('Bulgur Pilavı', 'Domatesli ve biberli bulgur pilavı.', 'SIDE_DISH', 25, 4, 480.00, 14.00, 88.00, 8.00, 10.00, true),
('Patates Kızartması', 'Çıtır patates kızartması.', 'SIDE_DISH', 30, 4, 640.00, 8.00, 72.00, 36.00, 6.00, true),
('Makarna Salatası', 'Yoğurtlu ve sebzeli makarna salatası.', 'SIDE_DISH', 25, 4, 560.00, 16.00, 78.00, 18.00, 6.00, true),
('Havuç Tarator', 'Yoğurt ve sarımsaklı havuç mezesi.', 'SIDE_DISH', 20, 4, 320.00, 10.00, 24.00, 20.00, 6.00, true),
('Cacık', 'Yoğurt, salatalık ve nane ile ferahlatıcı meze.', 'SIDE_DISH', 10, 4, 200.00, 10.00, 12.00, 10.00, 2.00, true),
('Yoğurtlu Semizotu', 'Semizotu ve yoğurtla hafif bir eşlikçi.', 'SIDE_DISH', 15, 4, 220.00, 9.00, 10.00, 12.00, 4.00, true),
('Mevsim Salata', 'Mevsim yeşillikleri ve domatesle karışık salata.', 'SIDE_DISH', 10, 4, 180.00, 6.00, 20.00, 8.00, 6.00, true),
('Rus Salatası', 'Patates ve bezelye ile mayonezli salata.', 'SIDE_DISH', 25, 4, 420.00, 10.00, 34.00, 24.00, 5.00, true),
('Zeytinyağlı Barbunya', 'Barbunya ve sebzelerle hazırlanan zeytinyağlı.', 'SIDE_DISH', 45, 4, 480.00, 16.00, 58.00, 20.00, 12.00, true),
('Fırın Patates', 'Baharatlı fırın patates.', 'SIDE_DISH', 35, 4, 520.00, 8.00, 68.00, 22.00, 8.00, true),
('İç Pilav', 'Kuş üzümlü ve dolmalık iç pilav.', 'SIDE_DISH', 40, 4, 640.00, 14.00, 96.00, 18.00, 6.00, true),
('Haydari', 'Süzme yoğurtla yapılan yoğun meze.', 'SIDE_DISH', 10, 4, 280.00, 12.00, 8.00, 18.00, 1.00, true),
('Çoban Salata', 'Domates, salatalık ve soğanla klasik salata.', 'SIDE_DISH', 10, 4, 160.00, 4.00, 18.00, 8.00, 5.00, true),
('Kısır', 'Bulgur, nar ekşisi ve yeşillikle yapılan salata.', 'SIDE_DISH', 25, 4, 460.00, 12.00, 72.00, 12.00, 10.00, true);

-- Recipe ingredients (strictly used for availability filtering)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit, optional) VALUES
((SELECT id FROM recipes WHERE name = 'Mercimek Çorbası'), (SELECT id FROM ingredients WHERE name = 'Kırmızı Mercimek'), 250, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Mercimek Çorbası'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 100, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Mercimek Çorbası'), (SELECT id FROM ingredients WHERE name = 'Havuç'), 100, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Mercimek Çorbası'), (SELECT id FROM ingredients WHERE name = 'Zeytinyağı'), 30, 'MILLILITER', false),
((SELECT id FROM recipes WHERE name = 'Mercimek Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 10, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Ezogelin Çorbası'), (SELECT id FROM ingredients WHERE name = 'Kırmızı Mercimek'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Ezogelin Çorbası'), (SELECT id FROM ingredients WHERE name = 'Bulgur'), 100, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Ezogelin Çorbası'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 100, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Ezogelin Çorbası'), (SELECT id FROM ingredients WHERE name = 'Domates'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Ezogelin Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 10, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Tarhana Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tarhana'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Tarhana Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tereyağı'), 30, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Tarhana Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 8, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Yayla Çorbası'), (SELECT id FROM ingredients WHERE name = 'Yoğurt'), 400, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Yayla Çorbası'), (SELECT id FROM ingredients WHERE name = 'Pirinç'), 80, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Yayla Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tereyağı'), 20, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Yayla Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 8, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Domates Çorbası'), (SELECT id FROM ingredients WHERE name = 'Domates'), 400, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Domates Çorbası'), (SELECT id FROM ingredients WHERE name = 'Un'), 30, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Domates Çorbası'), (SELECT id FROM ingredients WHERE name = 'Süt'), 300, 'MILLILITER', false),
((SELECT id FROM recipes WHERE name = 'Domates Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tereyağı'), 20, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Domates Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 8, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Tavuk Suyu Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tavuk Göğsü'), 300, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Tavuk Suyu Çorbası'), (SELECT id FROM ingredients WHERE name = 'Havuç'), 100, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Tavuk Suyu Çorbası'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 100, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Tavuk Suyu Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 8, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Şehriye Çorbası'), (SELECT id FROM ingredients WHERE name = 'Arpa Şehriye'), 120, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Şehriye Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tereyağı'), 20, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Şehriye Çorbası'), (SELECT id FROM ingredients WHERE name = 'Domates'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Şehriye Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 8, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'İşkembe Çorbası'), (SELECT id FROM ingredients WHERE name = 'İşkembe'), 400, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'İşkembe Çorbası'), (SELECT id FROM ingredients WHERE name = 'Sarımsak'), 10, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'İşkembe Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 8, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'İşkembe Çorbası'), (SELECT id FROM ingredients WHERE name = 'Limon'), 1, 'PIECE', false),

((SELECT id FROM recipes WHERE name = 'Kremalı Mantarlı Çorba'), (SELECT id FROM ingredients WHERE name = 'Mantar'), 250, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Kremalı Mantarlı Çorba'), (SELECT id FROM ingredients WHERE name = 'Krema'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Kremalı Mantarlı Çorba'), (SELECT id FROM ingredients WHERE name = 'Un'), 30, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Kremalı Mantarlı Çorba'), (SELECT id FROM ingredients WHERE name = 'Tereyağı'), 20, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Kremalı Mantarlı Çorba'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 6, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Sebze Çorbası'), (SELECT id FROM ingredients WHERE name = 'Patates'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Sebze Çorbası'), (SELECT id FROM ingredients WHERE name = 'Havuç'), 100, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Sebze Çorbası'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 100, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Sebze Çorbası'), (SELECT id FROM ingredients WHERE name = 'Brokoli'), 1, 'PIECE', false),
((SELECT id FROM recipes WHERE name = 'Sebze Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 8, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Brokoli Çorbası'), (SELECT id FROM ingredients WHERE name = 'Brokoli'), 1, 'PIECE', false),
((SELECT id FROM recipes WHERE name = 'Brokoli Çorbası'), (SELECT id FROM ingredients WHERE name = 'Patates'), 120, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Brokoli Çorbası'), (SELECT id FROM ingredients WHERE name = 'Süt'), 250, 'MILLILITER', false),
((SELECT id FROM recipes WHERE name = 'Brokoli Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 6, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Düğün Çorbası'), (SELECT id FROM ingredients WHERE name = 'Kuzu Eti'), 250, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Düğün Çorbası'), (SELECT id FROM ingredients WHERE name = 'Yoğurt'), 300, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Düğün Çorbası'), (SELECT id FROM ingredients WHERE name = 'Un'), 30, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Düğün Çorbası'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 8, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Düğün Çorbası'), (SELECT id FROM ingredients WHERE name = 'Limon'), 1, 'PIECE', false);

INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit, optional) VALUES
((SELECT id FROM recipes WHERE name = 'Karnıyarık'), (SELECT id FROM ingredients WHERE name = 'Patlıcan'), 500, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Karnıyarık'), (SELECT id FROM ingredients WHERE name = 'Dana Kıyma'), 250, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Karnıyarık'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Karnıyarık'), (SELECT id FROM ingredients WHERE name = 'Domates'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Karnıyarık'), (SELECT id FROM ingredients WHERE name = 'Biber'), 100, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Karnıyarık'), (SELECT id FROM ingredients WHERE name = 'Sarımsak'), 10, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'İmam Bayıldı'), (SELECT id FROM ingredients WHERE name = 'Patlıcan'), 500, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'İmam Bayıldı'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'İmam Bayıldı'), (SELECT id FROM ingredients WHERE name = 'Domates'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'İmam Bayıldı'), (SELECT id FROM ingredients WHERE name = 'Sarımsak'), 10, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'İmam Bayıldı'), (SELECT id FROM ingredients WHERE name = 'Zeytinyağı'), 50, 'MILLILITER', false),

((SELECT id FROM recipes WHERE name = 'Hünkar Beğendi'), (SELECT id FROM ingredients WHERE name = 'Patlıcan'), 400, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Hünkar Beğendi'), (SELECT id FROM ingredients WHERE name = 'Kuzu Eti'), 300, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Hünkar Beğendi'), (SELECT id FROM ingredients WHERE name = 'Süt'), 300, 'MILLILITER', false),
((SELECT id FROM recipes WHERE name = 'Hünkar Beğendi'), (SELECT id FROM ingredients WHERE name = 'Un'), 40, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Hünkar Beğendi'), (SELECT id FROM ingredients WHERE name = 'Tereyağı'), 30, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Kuzu İncik'), (SELECT id FROM ingredients WHERE name = 'Kuzu Eti'), 400, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Kuzu İncik'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Kuzu İncik'), (SELECT id FROM ingredients WHERE name = 'Havuç'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Kuzu İncik'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 8, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Etli Nohut'), (SELECT id FROM ingredients WHERE name = 'Nohut'), 250, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Etli Nohut'), (SELECT id FROM ingredients WHERE name = 'Kuzu Eti'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Etli Nohut'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Etli Nohut'), (SELECT id FROM ingredients WHERE name = 'Domates'), 200, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Etli Kuru Fasulye'), (SELECT id FROM ingredients WHERE name = 'Kuru Fasulye'), 250, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Etli Kuru Fasulye'), (SELECT id FROM ingredients WHERE name = 'Dana Kıyma'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Etli Kuru Fasulye'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Etli Kuru Fasulye'), (SELECT id FROM ingredients WHERE name = 'Domates'), 200, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Izgara Köfte'), (SELECT id FROM ingredients WHERE name = 'Dana Kıyma'), 300, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Izgara Köfte'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 100, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Izgara Köfte'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 8, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Izgara Köfte'), (SELECT id FROM ingredients WHERE name = 'Karabiber'), 4, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Fırında Tavuk'), (SELECT id FROM ingredients WHERE name = 'Tavuk Göğsü'), 500, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Fırında Tavuk'), (SELECT id FROM ingredients WHERE name = 'Zeytinyağı'), 40, 'MILLILITER', false),
((SELECT id FROM recipes WHERE name = 'Fırında Tavuk'), (SELECT id FROM ingredients WHERE name = 'Sarımsak'), 10, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Fırında Tavuk'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 8, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Fırında Tavuk'), (SELECT id FROM ingredients WHERE name = 'Karabiber'), 4, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Tas Kebabı'), (SELECT id FROM ingredients WHERE name = 'Kuzu Eti'), 350, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Tas Kebabı'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Tas Kebabı'), (SELECT id FROM ingredients WHERE name = 'Patates'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Tas Kebabı'), (SELECT id FROM ingredients WHERE name = 'Havuç'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Tas Kebabı'), (SELECT id FROM ingredients WHERE name = 'Domates'), 200, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Mantı'), (SELECT id FROM ingredients WHERE name = 'Un'), 300, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Mantı'), (SELECT id FROM ingredients WHERE name = 'Yumurta'), 1, 'PIECE', false),
((SELECT id FROM recipes WHERE name = 'Mantı'), (SELECT id FROM ingredients WHERE name = 'Dana Kıyma'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Mantı'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 100, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Mantı'), (SELECT id FROM ingredients WHERE name = 'Yoğurt'), 200, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Perde Pilavı'), (SELECT id FROM ingredients WHERE name = 'Pirinç'), 250, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Perde Pilavı'), (SELECT id FROM ingredients WHERE name = 'Tavuk Göğsü'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Perde Pilavı'), (SELECT id FROM ingredients WHERE name = 'Tereyağı'), 30, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Perde Pilavı'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 100, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Perde Pilavı'), (SELECT id FROM ingredients WHERE name = 'Un'), 100, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Etli Taze Fasulye'), (SELECT id FROM ingredients WHERE name = 'Taze Fasulye'), 300, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Etli Taze Fasulye'), (SELECT id FROM ingredients WHERE name = 'Dana Kıyma'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Etli Taze Fasulye'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Etli Taze Fasulye'), (SELECT id FROM ingredients WHERE name = 'Domates'), 200, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Patlıcan Musakka'), (SELECT id FROM ingredients WHERE name = 'Patlıcan'), 400, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Patlıcan Musakka'), (SELECT id FROM ingredients WHERE name = 'Dana Kıyma'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Patlıcan Musakka'), (SELECT id FROM ingredients WHERE name = 'Domates'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Patlıcan Musakka'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 150, 'GRAM', false);

INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit, optional) VALUES
((SELECT id FROM recipes WHERE name = 'Tavuk Sote'), (SELECT id FROM ingredients WHERE name = 'Tavuk Göğsü'), 400, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Tavuk Sote'), (SELECT id FROM ingredients WHERE name = 'Biber'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Tavuk Sote'), (SELECT id FROM ingredients WHERE name = 'Domates'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Tavuk Sote'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 150, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Çoban Kavurma'), (SELECT id FROM ingredients WHERE name = 'Kuzu Eti'), 350, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Çoban Kavurma'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Çoban Kavurma'), (SELECT id FROM ingredients WHERE name = 'Biber'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Çoban Kavurma'), (SELECT id FROM ingredients WHERE name = 'Domates'), 200, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Fırında Somon'), (SELECT id FROM ingredients WHERE name = 'Somon Fileto'), 400, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Fırında Somon'), (SELECT id FROM ingredients WHERE name = 'Limon'), 1, 'PIECE', false),
((SELECT id FROM recipes WHERE name = 'Fırında Somon'), (SELECT id FROM ingredients WHERE name = 'Zeytinyağı'), 30, 'MILLILITER', false),
((SELECT id FROM recipes WHERE name = 'Fırında Somon'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 6, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Zeytinyağlı Enginar'), (SELECT id FROM ingredients WHERE name = 'Enginar'), 2, 'PIECE', false),
((SELECT id FROM recipes WHERE name = 'Zeytinyağlı Enginar'), (SELECT id FROM ingredients WHERE name = 'Domates'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Zeytinyağlı Enginar'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 100, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Zeytinyağlı Enginar'), (SELECT id FROM ingredients WHERE name = 'Zeytinyağı'), 40, 'MILLILITER', false),
((SELECT id FROM recipes WHERE name = 'Zeytinyağlı Enginar'), (SELECT id FROM ingredients WHERE name = 'Limon'), 1, 'PIECE', false),

((SELECT id FROM recipes WHERE name = 'Etli Lahana Sarma'), (SELECT id FROM ingredients WHERE name = 'Lahana'), 1, 'PIECE', false),
((SELECT id FROM recipes WHERE name = 'Etli Lahana Sarma'), (SELECT id FROM ingredients WHERE name = 'Dana Kıyma'), 250, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Etli Lahana Sarma'), (SELECT id FROM ingredients WHERE name = 'Pirinç'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Etli Lahana Sarma'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 150, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Kıymalı Ispanak'), (SELECT id FROM ingredients WHERE name = 'Ispanak'), 400, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Kıymalı Ispanak'), (SELECT id FROM ingredients WHERE name = 'Dana Kıyma'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Kıymalı Ispanak'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 150, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Sebzeli Güveç'), (SELECT id FROM ingredients WHERE name = 'Patates'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Sebzeli Güveç'), (SELECT id FROM ingredients WHERE name = 'Patlıcan'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Sebzeli Güveç'), (SELECT id FROM ingredients WHERE name = 'Biber'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Sebzeli Güveç'), (SELECT id FROM ingredients WHERE name = 'Domates'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Sebzeli Güveç'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 150, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Pilav'), (SELECT id FROM ingredients WHERE name = 'Pirinç'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Pilav'), (SELECT id FROM ingredients WHERE name = 'Tereyağı'), 30, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Pilav'), (SELECT id FROM ingredients WHERE name = 'Arpa Şehriye'), 40, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Pilav'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 6, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Bulgur Pilavı'), (SELECT id FROM ingredients WHERE name = 'Bulgur'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Bulgur Pilavı'), (SELECT id FROM ingredients WHERE name = 'Domates'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Bulgur Pilavı'), (SELECT id FROM ingredients WHERE name = 'Biber'), 100, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Bulgur Pilavı'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 100, 'GRAM', false);

INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit, optional) VALUES
((SELECT id FROM recipes WHERE name = 'Patates Kızartması'), (SELECT id FROM ingredients WHERE name = 'Patates'), 400, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Patates Kızartması'), (SELECT id FROM ingredients WHERE name = 'Zeytinyağı'), 80, 'MILLILITER', false),
((SELECT id FROM recipes WHERE name = 'Patates Kızartması'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 6, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Makarna Salatası'), (SELECT id FROM ingredients WHERE name = 'Makarna'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Makarna Salatası'), (SELECT id FROM ingredients WHERE name = 'Yoğurt'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Makarna Salatası'), (SELECT id FROM ingredients WHERE name = 'Maydanoz'), 1, 'PIECE', false),

((SELECT id FROM recipes WHERE name = 'Havuç Tarator'), (SELECT id FROM ingredients WHERE name = 'Havuç'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Havuç Tarator'), (SELECT id FROM ingredients WHERE name = 'Yoğurt'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Havuç Tarator'), (SELECT id FROM ingredients WHERE name = 'Sarımsak'), 8, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Cacık'), (SELECT id FROM ingredients WHERE name = 'Yoğurt'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Cacık'), (SELECT id FROM ingredients WHERE name = 'Salatalık'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Cacık'), (SELECT id FROM ingredients WHERE name = 'Sarımsak'), 6, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Yoğurtlu Semizotu'), (SELECT id FROM ingredients WHERE name = 'Semizotu'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Yoğurtlu Semizotu'), (SELECT id FROM ingredients WHERE name = 'Yoğurt'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Yoğurtlu Semizotu'), (SELECT id FROM ingredients WHERE name = 'Sarımsak'), 6, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Mevsim Salata'), (SELECT id FROM ingredients WHERE name = 'Domates'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Mevsim Salata'), (SELECT id FROM ingredients WHERE name = 'Salatalık'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Mevsim Salata'), (SELECT id FROM ingredients WHERE name = 'Biber'), 80, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Mevsim Salata'), (SELECT id FROM ingredients WHERE name = 'Limon'), 1, 'PIECE', false),

((SELECT id FROM recipes WHERE name = 'Rus Salatası'), (SELECT id FROM ingredients WHERE name = 'Patates'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Rus Salatası'), (SELECT id FROM ingredients WHERE name = 'Havuç'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Rus Salatası'), (SELECT id FROM ingredients WHERE name = 'Bezelye'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Rus Salatası'), (SELECT id FROM ingredients WHERE name = 'Yoğurt'), 150, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Zeytinyağlı Barbunya'), (SELECT id FROM ingredients WHERE name = 'Barbunya'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Zeytinyağlı Barbunya'), (SELECT id FROM ingredients WHERE name = 'Domates'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Zeytinyağlı Barbunya'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 100, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Zeytinyağlı Barbunya'), (SELECT id FROM ingredients WHERE name = 'Zeytinyağı'), 30, 'MILLILITER', false),

((SELECT id FROM recipes WHERE name = 'Fırın Patates'), (SELECT id FROM ingredients WHERE name = 'Patates'), 400, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Fırın Patates'), (SELECT id FROM ingredients WHERE name = 'Zeytinyağı'), 40, 'MILLILITER', false),
((SELECT id FROM recipes WHERE name = 'Fırın Patates'), (SELECT id FROM ingredients WHERE name = 'Tuz'), 6, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'İç Pilav'), (SELECT id FROM ingredients WHERE name = 'Pirinç'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'İç Pilav'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 100, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'İç Pilav'), (SELECT id FROM ingredients WHERE name = 'Tereyağı'), 25, 'GRAM', false),

((SELECT id FROM recipes WHERE name = 'Haydari'), (SELECT id FROM ingredients WHERE name = 'Yoğurt'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Haydari'), (SELECT id FROM ingredients WHERE name = 'Sarımsak'), 6, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Haydari'), (SELECT id FROM ingredients WHERE name = 'Dereotu'), 1, 'PIECE', false),

((SELECT id FROM recipes WHERE name = 'Çoban Salata'), (SELECT id FROM ingredients WHERE name = 'Domates'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Çoban Salata'), (SELECT id FROM ingredients WHERE name = 'Salatalık'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Çoban Salata'), (SELECT id FROM ingredients WHERE name = 'Soğan'), 80, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Çoban Salata'), (SELECT id FROM ingredients WHERE name = 'Limon'), 1, 'PIECE', false),

((SELECT id FROM recipes WHERE name = 'Kısır'), (SELECT id FROM ingredients WHERE name = 'Bulgur'), 200, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Kısır'), (SELECT id FROM ingredients WHERE name = 'Domates'), 150, 'GRAM', false),
((SELECT id FROM recipes WHERE name = 'Kısır'), (SELECT id FROM ingredients WHERE name = 'Limon'), 1, 'PIECE', false),
((SELECT id FROM recipes WHERE name = 'Kısır'), (SELECT id FROM ingredients WHERE name = 'Maydanoz'), 1, 'PIECE', false);

