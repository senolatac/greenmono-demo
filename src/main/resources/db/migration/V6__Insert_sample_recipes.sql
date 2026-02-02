-- Insert sample Turkish recipes

-- Insert sample ingredients first (if not exist)
INSERT INTO ingredients (name, category, quantity, unit, available, user_id) VALUES
('Un', 'GRAINS', 1000, 'GRAM', true, null),
('Yumurta', 'DAIRY', 10, 'PIECE', true, null),
('Kıyma', 'MEAT', 500, 'GRAM', true, null),
('Soğan', 'VEGETABLES', 5, 'PIECE', true, null),
('Yoğurt', 'DAIRY', 1, 'KILOGRAM', true, null),
('Tereyağı', 'DAIRY', 250, 'GRAM', true, null),
('Patlıcan', 'VEGETABLES', 6, 'PIECE', true, null),
('Domates', 'VEGETABLES', 5, 'PIECE', true, null),
('Biber', 'VEGETABLES', 4, 'PIECE', true, null),
('Pirinç', 'GRAINS', 1, 'KILOGRAM', true, null),
('Tavuk Göğsü', 'POULTRY', 500, 'GRAM', true, null),
('Makarna', 'GRAINS', 500, 'GRAM', true, null),
('Patates', 'VEGETABLES', 1, 'KILOGRAM', true, null),
('Tuz', 'HERBS_SPICES', 100, 'GRAM', true, null),
('Karabiber', 'HERBS_SPICES', 50, 'GRAM', true, null),
('Zeytinyağı', 'OILS_FATS', 500, 'MILLILITER', true, null),
('Sarımsak', 'HERBS_SPICES', 100, 'GRAM', true, null),
('Kuşbaşı Et', 'MEAT', 500, 'GRAM', true, null),
('Havuç', 'VEGETABLES', 5, 'PIECE', true, null),
('Bezelye', 'VEGETABLES', 200, 'GRAM', true, null),
('Lahana', 'VEGETABLES', 1, 'PIECE', true, null),
('Kırmızı Biber', 'HERBS_SPICES', 30, 'GRAM', true, null)
ON CONFLICT DO NOTHING;

-- Recipe 1: Mantı (Turkish Dumplings)
INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Mantı', 'Geleneksel Türk mantısı. İnce hamur içinde kıymalı dolgulu, yoğurt ve tereyağlı sosla servis edilen nefis bir ana yemek.', 'MAIN_COURSE', 90, 4, 520.00, 28.00, 65.00, 18.00, 3.50, true);

-- Recipe 2: Mercimek Çorbası (Lentil Soup)
INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Mercimek Çorbası', 'Türk mutfağının vazgeçilmez çorbalarından. Kırmızı mercimek, havuç ve soğanla hazırlanan besleyici ve lezzetli bir çorba.', 'SOUP', 35, 6, 180.00, 9.00, 28.00, 4.50, 7.00, true);

-- Recipe 3: Hünkar Beğendi (Sultan''s Delight)
INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Hünkar Beğendi', 'Patlıcan püresi üzerine kuşbaşı et haşlama. Osmanlı sarayından gelen özel bir ana yemek.', 'MAIN_COURSE', 75, 4, 450.00, 32.00, 22.00, 28.00, 6.00, true);

-- Recipe 4: Fırında Kapuşka (Baked Cabbage)
INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Fırında Kapuşka', 'Lahana, kıyma ve pirinçle hazırlanan fırında pişirilmiş geleneksel Türk yemeği.', 'MAIN_COURSE', 60, 6, 280.00, 18.00, 24.00, 12.00, 5.50, true);

-- Recipe 5: Karnıyarık (Stuffed Eggplant)
INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Karnıyarık', 'Közlenmiş patlıcanın içine kıymalı soğanlı harç doldurularak fırında pişirilen klasik Türk yemeği.', 'MAIN_COURSE', 70, 4, 380.00, 22.00, 28.00, 20.00, 8.00, true);

-- Recipe 6: Perde Pilavı (Veiled Pilaf)
INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Perde Pilavı', 'İçi tavuklu, bademli ve üzümlü pilavın ince yufka ile kaplanarak fırında pişirilmesi ile yapılan özel pilav.', 'MAIN_COURSE', 80, 6, 420.00, 24.00, 58.00, 14.00, 3.00, true);

-- Recipe 7: Patates Kızartması (French Fries)
INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Patates Kızartması', 'Çıtır çıtır kızarmış patates dilimler. Her yaştan sevilen klasik atıştırmalık.', 'SIDE_DISH', 30, 4, 312.00, 4.00, 42.00, 15.00, 4.20, true);

-- Recipe 8: Fırın Tavuk (Baked Chicken)
INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Fırın Tavuk', 'Baharatlarla marine edilip fırında sütlü pişirilen yumuşacık tavuk göğsü.', 'MAIN_COURSE', 55, 4, 285.00, 38.00, 8.00, 11.00, 1.50, true);

-- Recipe 9: Makarna (Pasta with Butter)
INSERT INTO recipes (name, description, category, cooking_time_minutes, servings, calories, protein, carbohydrates, fat, fiber, active) VALUES
('Tereyağlı Makarna', 'Haşlanmış makarnanın tereyağı ve kaşarla harmanlanması. Basit ama lezzetli.', 'MAIN_COURSE', 20, 4, 380.00, 12.00, 58.00, 12.00, 3.00, true);

-- Insert recipe instructions
INSERT INTO recipe_instructions (recipe_id, instruction, step_order) VALUES
-- Mantı instructions
(1, 'Un, yumurta, tuz ve su ile hamur yoğurun. 30 dakika dinlendirin.', 0),
(1, 'Kıyma, soğan, tuz ve karabiberle iç harcı hazırlayın.', 1),
(1, 'Hamuru ince açın ve küçük kareler kesin.', 2),
(1, 'Her kareye iç harçtan koyup kenarlarını kapatın.', 3),
(1, 'Kaynar tuzlu suda pişirin. Yoğurt ve tereyağlı sosla servis edin.', 4),

-- Mercimek Çorbası instructions
(2, 'Kırmızı mercimek, soğan ve havuçları doğrayın.', 0),
(2, 'Tereyağında soğan ve havucu kavurun.', 1),
(2, 'Mercimek ve suyu ekleyip kaynatın.', 2),
(2, 'Yumuşayınca blenderdan geçirin. Baharatları ekleyip servis edin.', 3),

-- Hünkar Beğendi instructions
(3, 'Patlıcanları közleyip kabuklarını soyun ve püre yapın.', 0),
(3, 'Tereyağında un kavurup süt ekleyin, beşamel sos yapın.', 1),
(3, 'Patlıcan püresini beşamele karıştırın.', 2),
(3, 'Kuşbaşı eti soğan ve domatesle pişirin.', 3),
(3, 'Patlıcan püresi üzerine eti yerleştirip servis edin.', 4),

-- Fırında Kapuşka instructions
(4, 'Lahanayı ince kıyın ve tuzlu suda haşlayın.', 0),
(4, 'Kıyma, soğan, pirinç ve baharatları karıştırın.', 1),
(4, 'Lahana ile iç harcı kat kat fırın kabına dizin.', 2),
(4, 'Üzerine domates sosu dökün ve 180°C fırında 45 dakika pişirin.', 3),

-- Karnıyarık instructions
(5, 'Patlıcanları közleyip ortasını açın.', 0),
(5, 'Kıyma, soğan, domates ve biberi kavurun.', 1),
(5, 'Kıymalı harcı patlıcanların içine doldurun.', 2),
(5, 'Fırın tepsisine dizin, üzerine domates dilimleri koyun.', 3),
(5, '180°C fırında 40 dakika pişirin.', 4),

-- Perde Pilavı instructions
(6, 'Tavuk haşlayıp didikleyin. Pirinç kavurun.', 0),
(6, 'Pirinç, tavuk, badem, kuş üzümü ve baharatları karıştırın.', 1),
(6, 'Yufkayı yağlayıp fırın kabına yerleştirin.', 2),
(6, 'Pilavı yufkanın içine koyun ve üstünü örtün.', 3),
(6, '180°C fırında 35 dakika pişirin.', 4),

-- Patates Kızartması instructions
(7, 'Patatesleri soyun ve çubuk şeklinde kesin.', 0),
(7, 'Soğuk suda bekletin ve kurulayın.', 1),
(7, 'Kızgın yağda altın sarısı olana kadar kızartın.', 2),
(7, 'Kağıt havluya alıp tuzlayın ve sıcak servis edin.', 3),

-- Fırın Tavuk instructions
(8, 'Tavuk göğüslerini yıkayın ve kurulayın.', 0),
(8, 'Yoğurt, sarımsak, zeytinyağı ve baharatlarla marine edin.', 1),
(8, 'En az 2 saat buzdolabında dinlendirin.', 2),
(8, 'Fırın tepsisine dizin ve 180°C fırında 45 dakika pişirin.', 3),

-- Makarna instructions
(9, 'Bol tuzlu suda makarnayı haşlayın.', 0),
(9, 'Süzün ve tereyağı ile karıştırın.', 1),
(9, 'İsteğe göre kaşar rendesi ekleyin.', 2),
(9, 'Sıcak servis edin.', 3);

-- Note: Recipe ingredients will be added through the application service layer
-- as they require mapping to actual ingredient IDs which may vary
