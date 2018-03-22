INSERT INTO USERS (id, username, password, first_name, last_name, email, phone_number, enabled, last_password_reset_date) VALUES (1, 'user', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'USUARIO', 'NORMAL', 'user@example.com', '+1234567890', true, '2017-10-01 21:58:58');
INSERT INTO USERS (id, username, password, first_name, last_name, email, phone_number, enabled, last_password_reset_date) VALUES (2, 'admin', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'USUARIO', 'ADMIN', 'admin@example.com', '+0987654321', true, '2017-10-01 18:57:58');

INSERT INTO AUTHORITY (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO AUTHORITY (id, name) VALUES (2, 'ROLE_ADMIN');

INSERT INTO USER_AUTHORITY (user_id, authority_id) VALUES (1, 1);
INSERT INTO USER_AUTHORITY (user_id, authority_id) VALUES (2, 1);
INSERT INTO USER_AUTHORITY (user_id, authority_id) VALUES (2, 2);

INSERT INTO pdu (clave,cont,datos,tabla) VALUES ('PA',1,'Productes Agraris','FAMILIA'),('PAG',1,'Produces Agricoles','SUBFAMILIA'),('FR',1,'Fruita','GRUP'),('PI',1,'Pinyol','SUBGRUP'),('PR01',1,'Pressec                  PAPAGFRPI','PRODUCTE'),('PR01BL',1,'Blanca','COLORCARN'),('PR01GR',1,'Groga','COLORCARN'),('PR0101',1,'I','QUALITAT'),('PR0102',1,'II','QUALITAT'),('PR0103',1,'Industria','QUALITAT');
INSERT INTO pdu (clave,cont,datos,tabla) VALUES ('PR0101',1,'AAAA (> 90 mm)','CALIBRE'),('PR0102',1,'AAA (80-90 mm)','CALIBRE'),('PR0103',1,'AA (73-80 mm)','CALIBRE'),('PR0104',1,'A (67-73 mm)','CALIBRE'),('PR0105',1,'B (61-67 mm)','CALIBRE'),('PR0106',1,'C (56-61 mm)','CALIBRE'),('PR0107',1,'D (51-56 mm)','CALIBRE'),('PR02',1,'Pavia                    PAPAGFRPI','PRODUCTE'),('PR0201',1,'Groga','COLORCARN'),('PR0201',1,'I','QUALITAT');
INSERT INTO pdu (clave,cont,datos,tabla) VALUES ('PR0202',1,'II','QUALITAT'),('PR0203',1,'Industria','QUALITAT'),('PR0201',1,'AAAA (> 90 mm)','CALIBRE'),('PR0202',1,'AAA (80-90 mm)','CALIBRE'),('PR0203',1,'AA (73-80 mm)','CALIBRE'),('PR0204',1,'A (67-73 mm)','CALIBRE'),('PR0205',1,'B (61-67 mm)','CALIBRE'),('PR0206',1,'C (56-61 mm)','CALIBRE'),('PR0207',1,'D (51-56 mm)','CALIBRE');
INSERT INTO pdu (clave,cont,datos,tabla) VALUES ('PR0202',1,'II','QUALITAT');
INSERT INTO register (id,color_carn,periode,tipus_producte,varietat,calibre,empresa_informant,preu_sortida,qualitat,quantitat_venuda) VALUES (2, 'GR', '1', 'PR01', NULL, 'CA01', 'Nufri', 0.8, 'QU01', 90), (3, 'GR', '1', 'PR02', NULL, 'CA03', 'Nufri', 0.75, 'QU01', 68), (4, 'BL', '1', 'PR01', NULL, 'CA02', 'Nufri', 0.80, 'QU01', 57);
