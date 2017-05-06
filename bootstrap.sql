INSERT INTO login (username, sha_password, user_type, created_utc) VALUES ('caregiver', 'caregiver', 0, date(0));
INSERT INTO login (username, sha_password, user_type, created_utc) VALUES ('child', 'childchild', 1, CURDATE());

INSERT INTO caregiver (first_name, last_name, email, gender, caregiverlogin_id) 
VALUES ('Caregiver', '1', 'caregiver@caregiver.com', 'Female', 1);

INSERT INTO child VALUES (1, 'Child', '1', CURDATE(), 'Male', 2);

INSERT into caregiverchild VALUES (1, 1);
select * from exercise_answer;
INSERT INTO topic (description) VALUES ('Alimentos'),('Animais'),('Emoções'),('Transportes');
INSERT INTO level (description) VALUES ('Fácil'),('Médio'),('Difícil');
INSERT into sequence VALUES (1, 1, 'Primeira sequencia');

INSERT INTO personal_message_type VALUES (1, 'Greeting');
INSERT INTO personal_message VALUES (1, 1, 'Bom dia, alegria!', 1);


select * from login;
select * from child;
-- select * from caregiver;
-- select * from caregiverchild;
-- select * from personal_message_type;
-- select * from personal_message;
select * from resource;
select * from resource_area;
INSERT INTO resource (resource_path, owner_id) VALUES ('images/stimuli/laranja.png', 1), ('images/stimuli/banana.png', 1), ('images/stimuli/maça.png', 1), ('images/stimuli/limão.png', 1), ('images/stimuli/morango.png', 1);
INSERT INTO resource_area (resource_area_description) VALUES ('stimuli'), ('answer'), ('option');
INSERT INTO resource_area (resource_area_description) VALUES ('reinforcement');
INSERT INTO resource (resource_path, resource_area_id, owner_id) VALUES ('images/reinforcement/joy.jpg', 4, 1), ('images/reinforcement/welldone.jpeg', 4, 1), ('images/reinforcement/yey.jpeg', 4, 1), ('images/reinforcement/yupi.jpg', 4, 1);
INSERT INTO resource (resource_path, resource_area_id, owner_id) VALUES ('images/reinforcement/joy_animated.gif', 4, 1);