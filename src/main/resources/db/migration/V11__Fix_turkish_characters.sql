-- Fix Turkish characters that were stored as Unicode escape sequences

-- Fix soup names and descriptions
UPDATE recipes SET name = 'Ezogelin Çorbası', description = 'Kırmızı mercimek, bulgur ve çeşitli baharatlarla hazırlanan geleneksel Türk çorbası.' WHERE name LIKE 'Ezogelin%orbas%';
UPDATE recipes SET name = 'Domates Çorbası', description = 'Taze domateslerle hazırlanan kremsi ve lezzetli bir çorba.' WHERE name LIKE 'Domates%orbas%';
UPDATE recipes SET name = 'Tarhana Çorbası', description = 'Kurutulmuş tarhana ile hazırlanan geleneksel Anadolu çorbası.' WHERE name LIKE 'Tarhana%orbas%';
UPDATE recipes SET name = 'Yayla Çorbası', description = 'Yoğurt ve pirinçle hazırlanan hafif ve besleyici bir çorba.' WHERE name LIKE 'Yayla%orbas%';

-- Fix side dish names and descriptions
UPDATE recipes SET description = 'Tereyağı ve şehriye ile hazırlanan klasik Türk pilavı.' WHERE name = 'Pilav';
UPDATE recipes SET name = 'Bulgur Pilavı', description = 'Domatesli ve biberli bulgur pilavı. Lif açısından zengin.' WHERE name LIKE 'Bulgur Pilav%';
UPDATE recipes SET name = 'Makarna Salatası', description = 'Renkli sebzeler ve zeytinyağı ile hazırlanan soğuk makarna salatası.' WHERE name LIKE 'Makarna Salatas%';
UPDATE recipes SET name = 'Havuç Tarator', description = 'Rendelenmiş havuç, yoğurt ve sarımsakla hazırlanan ferahlatıcı meze.' WHERE name LIKE 'Havu%Tarator%';
