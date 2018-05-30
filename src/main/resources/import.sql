INSERT INTO USERS (id, username, password, first_name, last_name, email, phone_number, enabled, last_password_reset_date) VALUES (1, 'user ABC', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'USUARIO', 'NORMAL', 'user@example.com', '+1234567890', true, '2017-10-01 21:58:58'),(2, 'admin', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'USUARIO', 'ADMIN', 'admin@example.com', '+0987654321', true, '2017-10-01 18:57:58'),(3, 'gestor', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'USUARIO', 'GESTOR', 'gestor@example.com', '+0987654321', true, '2017-10-01 18:57:58');

INSERT INTO AUTHORITY (id, name) VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN'), (3, 'ROLE_GESTOR');

INSERT INTO USER_AUTHORITY (user_id, authority_id) VALUES (1, 1),(2, 1),(2, 2),(2,3),(3,1),(3,3);

INSERT INTO pdu (clave,cont,datos,tabla) values ('PA',1,'Productes Agraris','FAMILIA'),
('PAG',1,'Produces Agricoles','SUBFAMILIA'),
('FR',1,'Fruita','GRUP'),
('PI',1,'Pinyol','SUBGRUP'),
('PR01',1,'Pressec                  PAPAGFRPI','PRODUCTE'),
('PR01BL',1,'Blanca','COLORCARN'),
('PR01GR',1,'Groga','COLORCARN'),
('PR0101',1,'I','QUALITAT'),
('PR0102',1,'II','QUALITAT'),
('PR0103',1,'Industria','QUALITAT'),
('PR0101',1,'AAAA (> 90 mm)','CALIBRE'),
('PR0102',1,'AAA (80-90 mm)','CALIBRE'),
('PR0103',1,'AA (73-80 mm)','CALIBRE'),
('PR0104',1,'A (67-73 mm)','CALIBRE'),
('PR0105',1,'B (61-67 mm)','CALIBRE'),
('PR0106',1,'C (56-61 mm)','CALIBRE'),
('PR0107',1,'D (51-56 mm)','CALIBRE'),
('PA01',1,'Pavia                    PAPAGFRPI','PRODUCTE'),
('PA01GR',1,'Groga','COLORCARN'),
('PA0101',1,'I','QUALITAT'),
('PA0102',1,'II','QUALITAT'),
('PA0103',1,'Industria','QUALITAT'),
('PA0101',1,'AAAA (> 90 mm)','CALIBRE'),
('PA0102',1,'AAA (80-90 mm)','CALIBRE'),
('PA0103',1,'AA (73-80 mm)','CALIBRE'),
('PA0104',1,'A (67-73 mm)','CALIBRE'),
('PA0105',1,'B (61-67 mm)','CALIBRE'),
('PA0106',1,'C (56-61 mm)','CALIBRE'),
('PA0107',1,'D (51-56 mm)','CALIBRE'),
('LL',1,'Llavor','SUBGRUP'),
('PO01',1,'Poma                     PAPAGFRLL','PRODUCTE')
,('PO0101',1,'I','QUALITAT')
,('PO0102',1,'II','QUALITAT')
,('PO0103',1,'Industria','QUALITAT')
,('PO0104',1,'Extra','QUALITAT')
,('PO0108',1,'80 i + mm','CALIBRE')
,('PO0109',1,'75-80 mm','CALIBRE')
,('PO0110',1,'70-75 mm','CALIBRE')
,('PO0111',1,'65-70 mm','CALIBRE')
,('PO0112',1,'60-65 mm','CALIBRE')
,('PO0101',1,'Golden Delicious','VARIETAT')
,('PO0102',1,'Gala','VARIETAT')
,('PO0103',1,'Granny Smith','VARIETAT')
,('PO0104',1,'Fuji','VARIETAT')
,('PO0105',1,'Cripps Pink','VARIETAT')
,('PO0106',1,'Vermelles Americanes','VARIETAT')
,('PR02',1,'Pressec Pla              PAPAGFRPI','PRODUCTE')
,('PR02BL',1,'Blanca','COLORCARN')
,('PR02GR',1,'Groga','COLORCARN')
,('PR0201',1,'I','QUALITAT')
,('PR0202',1,'II','QUALITAT')
,('PR0103',1,'Industria','QUALITAT')
,('PR0201',1,'AAAA (> 90 mm)','CALIBRE')
,('PR0202',1,'AAA (80-90 mm)','CALIBRE')
,('PR0203',1,'AA (73-80 mm)','CALIBRE')
,('PR0204',1,'A (67-73 mm)','CALIBRE')
,('PR0205',1,'B (61-67 mm)','CALIBRE')
,('PR0206',1,'C (56-61 mm)','CALIBRE')
,('PR0207',1,'D (51-56 mm)','CALIBRE')
,('NE01',1,'Nectarina                PAPAGFRPI','PRODUCTE')
,('NE01BL',1,'Blanca','COLORCARN')
,('PR01GR',1,'Groga','COLORCARN')
,('NE0101',1,'I','QUALITAT')
,('NE0102',1,'II','QUALITAT')
,('NE0103',1,'Industria','QUALITAT')
,('NE0101',1,'AAAA (> 90 mm)','CALIBRE')
,('NE0102',1,'AAA (80-90 mm)','CALIBRE')
,('NE0103',1,'AA (73-80 mm)','CALIBRE')
,('NE0104',1,'A (67-73 mm)','CALIBRE')
,('NE0105',1,'B (61-67 mm)','CALIBRE')
,('NE0106',1,'C (56-61 mm)','CALIBRE')
,('NE0107',1,'D (51-56 mm)','CALIBRE')
,('NE02',1,'Nectarina Plana          PAPAGFRPI','PRODUCTE')
,('NE02BL',1,'Blanca','COLORCARN')
,('PR01GR',1,'Groga','COLORCARN')
,('NE0201',1,'I','QUALITAT')
,('NE0202',1,'II','QUALITAT')
,('NE0203',1,'Industria','QUALITAT')
,('NE0201',1,'AAAA (> 90 mm)','CALIBRE')
,('NE0202',1,'AAA (80-90 mm)','CALIBRE')
,('NE0203',1,'AA (73-80 mm)','CALIBRE')
,('NE0204',1,'A (67-73 mm)','CALIBRE')
,('NE0205',1,'B (61-67 mm)','CALIBRE')
,('NE0206',1,'C (56-61 mm)','CALIBRE')
,('NE0207',1,'D (51-56 mm)','CALIBRE')
,('PE01',1,'Pera                     PAPAGFRLL','PRODUCTE')
,('PE0101',1,'Conference','VARIETAT')
,('PE0102',1,'Blanquilla','VARIETAT')
,('PE0103',1,'Llimonera','VARIETAT')
,('PE0104',1,'Ercolini','VARIETAT')
,('PE0105',1,'Williams','VARIETAT')
,('PE0106',1,'Alexandrina','VARIETAT')
,('PE0101',1,'I','QUALITAT')
,('PE0102',1,'II','QUALITAT')
,('PE0103',1,'Industria','QUALITAT')
,('PE0104',1,'Extra','QUALITAT')
,('PE0108',1,'80 i + mm','CALIBRE')
,('PE0109',1,'75-80 mm','CALIBRE')
,('PE0110',1,'70-75 mm','CALIBRE')
,('PE0111',1,'65-70 mm','CALIBRE')
,('PE0112',1,'60-65 mm','CALIBRE')
,('PE0113',1,'55-60 mm','CALIBRE')
,('PE0114',1,'50-55 mm','CALIBRE')
,('PE0115',1,'45-50 mm','CALIBRE')
;

INSERT INTO empressa (codi,estat) VALUES ('ABCDEF',1),
('GHIJKL',1),
('MNOPQR',0);

INSERT INTO empressa_producte (tipus_producte,empressa_id) VALUES ('Pressec',1),
('Pavia',1),
('Poma',2),
('Pressec',2);