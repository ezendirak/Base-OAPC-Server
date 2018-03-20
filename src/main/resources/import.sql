INSERT INTO USERS (id, username, password, first_name, last_name, email, phone_number, enabled, last_password_reset_date) VALUES (1, 'user', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'USUARIO', 'NORMAL', 'user@example.com', '+1234567890', true, '2017-10-01 21:58:58');
INSERT INTO USERS (id, username, password, first_name, last_name, email, phone_number, enabled, last_password_reset_date) VALUES (2, 'admin', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'USUARIO', 'ADMIN', 'admin@example.com', '+0987654321', true, '2017-10-01 18:57:58');

INSERT INTO AUTHORITY (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO AUTHORITY (id, name) VALUES (2, 'ROLE_ADMIN');

INSERT INTO USER_AUTHORITY (user_id, authority_id) VALUES (1, 1);
INSERT INTO USER_AUTHORITY (user_id, authority_id) VALUES (2, 1);
INSERT INTO USER_AUTHORITY (user_id, authority_id) VALUES (2, 2);

INSERT INTO pdu (clave,cont,datos,tabla) VALUES ('PA',1,'Productes Agraris','FAMILIA'),('PAG',1,'Produces Agricoles','SUBFAMILIA'),('FR',1,'Fruita','GRUP'),('PI',1,'Pinyol','SUBGRUP'),('PRO1',1,'Pressec                  PAPAGFRPI','PRODUCTE'),('PRO1BL',1,'Blanca','COLORCARN'),('PRO1GR',1,'Groga','COLORCARN'),('PRO101',1,'I','QUALITAT'),('PRO102',1,'II','QUALITAT'),('PRO103',1,'Industria','QUALITAT');
INSERT INTO pdu (clave,cont,datos,tabla) VALUES ('PRO101',1,'AAAA (> 90 mm)','CALIBRE'),('PRO102',1,'AAA (80-90 mm)','CALIBRE'),('PRO103',1,'AA (73-80 mm)','CALIBRE'),('PRO104',1,'A (67-73 mm)','CALIBRE'),('PRO105',1,'B (61-67 mm)','CALIBRE'),('PRO106',1,'C (56-61 mm)','CALIBRE'),('PRO107',1,'D (51-56 mm)','CALIBRE'),('PRO2',1,'Pavia                    PAPAGFRPI','PRODUCTE'),('PRO201',1,'Groga','COLORCARN'),('PRO201',1,'I','QUALITAT');
INSERT INTO pdu (clave,cont,datos,tabla) VALUES ('PRO202',1,'II','QUALITAT'),('PRO203',1,'Industria','QUALITAT'),('PRO201',1,'AAAA (> 90 mm)','CALIBRE'),('PRO202',1,'AAA (80-90 mm)','CALIBRE'),('PRO203',1,'AA (73-80 mm)','CALIBRE'),('PRO204',1,'A (67-73 mm)','CALIBRE'),('PRO205',1,'B (61-67 mm)','CALIBRE'),('PRO206',1,'C (56-61 mm)','CALIBRE'),('PRO207',1,'D (51-56 mm)','CALIBRE');
INSERT INTO pdu (clave,cont,datos,tabla) VALUES ('PRO202',1,'II','QUALITAT');
INSERT INTO register (id,color_carn,periode,tipus_producte,varietat,calibre,empresa_informant,preu_sortida,qualitat,quantitat_venuda) VALUES (2, 'Groga', '1', 'Pressec', NULL, 'AAAA (> 90 mm)', 'Nufri', 0.8, 'I', 90), (3, 'Groga', '1', 'Pavia', NULL, 'AA (73-80 mm)', 'Nufri', 0.75, 'I', 68), (4, 'PRO1BL', '1', 'Pressec', NULL, 'PRO102', 'Nufri', 0.80, 'PRO101', 57);
