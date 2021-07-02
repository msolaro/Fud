CREATE TABLE IF NOT EXISTS experiment (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(30),
  last_name VARCHAR(30),
  address VARCHAR(255),
  city VARCHAR(80),
  telephone VARCHAR(20),
  INDEX(last_name)
) engine=InnoDB;

INSERT IGNORE INTO owners VALUES (1,  'George', 'Franklin','110 W. Liberty St.', 'Madison', '6085551023'); 
INSERT IGNORE INTO owners VALUES (2, 'Betty', 'Davis', '638 Cardinal Ave.', 'San Pedro', '6085551749');
INSERT IGNORE INTO owners VALUES (3,  'Eduardo', 'Rodriquez','2693 Commerce St.', 'McFarland', '6085558763');

CREATE TABLE IF NOT EXISTS experiment (
  id INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(30),
  fav_food VARCHAR(30),
  INDEX(first_name)
) engine=InnoDB;

INSERT IGNORE INTO experiment VALUES (1,  'George', 'pizza'); 
INSERT IGNORE INTO experiment VALUES (2, 'Betty', 'ramen');
INSERT IGNORE INTO experiment VALUES (3,  'Eduardo', 'ramen');
INSERT IGNORE INTO experiment VALUES (4,  'Steve', 'cellphones'); 
INSERT IGNORE INTO experiment VALUES (5, 'Alejandro', 'burgers');
INSERT IGNORE INTO experiment VALUES (6,  'Grace', 'Apples');
