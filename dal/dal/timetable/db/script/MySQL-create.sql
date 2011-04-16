/***************************** Creating tables *****************************/

CREATE TABLE classroom(
ID INT NOT NULL AUTO_INCREMENT,
name VARCHAR( 50 ) NOT NULL,
capacity INT NOT NULL DEFAULT 50,
PRIMARY KEY pk_classroom ( ID ),
UNIQUE KEY uk_classroom ( name )
) ENGINE = InnoDB;

CREATE TABLE classroom_department(
ID INT NOT NULL AUTO_INCREMENT,
classroom_ID INT NOT NULL,
department_ID INT NOT NULL,
semester_ID INT NOT NULL,
PRIMARY KEY pk_classroom_department ( ID ),
UNIQUE KEY uk_classroom_department ( classroom_ID, department_ID, semester_ID )
) ENGINE = InnoDB;

CREATE TABLE constraint_attribute_type(
ID INT NOT NULL,
name VARCHAR( 20 ) NOT NULL,
PRIMARY KEY pk_constraint_attribute_type ( ID )
) ENGINE = InnoDB;

CREATE TABLE constraint_entity(
ID INT NOT NULL,
name VARCHAR( 50 ) NOT NULL,
display_name VARCHAR( 50 ) NOT NULL,
PRIMARY KEY pk_constraint_entity ( ID )
) ENGINE = InnoDB;

CREATE TABLE constraint_multivalue_property(
ID INT NOT NULL AUTO_INCREMENT,
left_constraint_entity_ID INT NOT NULL,
right_constraint_entity_ID INT NOT NULL,
entity_ID VARCHAR( 512 ) NOT NULL,
attribute_name VARCHAR( 250 ) NOT NULL,
attribute_value INT NOT NULL,
PRIMARY KEY pk_constraint_multivalue_property ( ID )
) ENGINE = InnoDB;

CREATE TABLE constraint_property(
ID INT NOT NULL AUTO_INCREMENT,
constraint_entity_ID INT NOT NULL,
entity_ID VARCHAR( 512 ) NOT NULL,
attribute_name VARCHAR( 250 ) NOT NULL,
attribute_value VARCHAR( 250 ) NOT NULL,
constraint_attribute_type_ID INT NOT NULL,
PRIMARY KEY pk_constraint_property ( ID ),
UNIQUE KEY uk_constraint_property ( constraint_entity_ID, entity_ID, attribute_name )
) ENGINE = InnoDB;

CREATE TABLE constraint_timeslot_type(
ID INT NOT NULL,
constraint_type_ID INT NOT NULL,
name VARCHAR( 50 ) NOT NULL,
PRIMARY KEY pk_constraint_timeslot_type ( ID )
) ENGINE = InnoDB;

CREATE TABLE constraint_type(
ID INT NOT NULL,
name VARCHAR( 50 ),
PRIMARY KEY pk_constraint_type ( ID )
) ENGINE = InnoDB;

CREATE TABLE constraint_type_entity(
ID INT NOT NULL,
constraint_type_ID INT NOT NULL,
constraint_entity_ID INT NOT NULL,
position INT NOT NULL,
PRIMARY KEY pk_constraint_type_entity ( ID )
) ENGINE = InnoDB;

CREATE TABLE constraint_type_operator(
ID INT NOT NULL,
constraint_type_ID INT NOT NULL,
name VARCHAR( 50 ) NOT NULL,
operator_sign VARCHAR( 10 ) NOT NULL,
PRIMARY KEY pk_constraint_type_operator ( ID )
) ENGINE = InnoDB;

CREATE TABLE constrainttimetable(
ID INT NOT NULL AUTO_INCREMENT,
name VARCHAR( 1024 ) NOT NULL,
apply BOOLEAN NOT NULL,
ishard BOOLEAN NULL,
PRIMARY KEY pk_constrainttimetable ( ID )
) ENGINE = InnoDB;

CREATE TABLE course(
ID VARCHAR( 10 ) NOT NULL,
name VARCHAR( 50 ) NOT NULL,
capacity INT NOT NULL DEFAULT 0,
duration VARCHAR ( 10 ) NOT NULL,
lesson_count INT NOT NULL,
PRIMARY KEY pk_course ( ID )
) ENGINE = InnoDB;

CREATE TABLE course_section(
ID INT NOT NULL AUTO_INCREMENT,
semester_ID INT NOT NULL,
department_ID INT NOT NULL,
course_ID VARCHAR( 10 ) NOT NULL,
section_name VARCHAR( 5 ) NOT NULL,
PRIMARY KEY pk_course_section ( ID ),
UNIQUE KEY uk_course_section ( course_ID, section_name, semester_ID, department_ID )
) ENGINE = InnoDB;

CREATE TABLE department(
ID INT NOT NULL AUTO_INCREMENT,
name VARCHAR( 50 ),
PRIMARY KEY pk_department( ID ),
UNIQUE KEY uk_department( name )
) ENGINE = InnoDB;

CREATE TABLE enclosure_constraint(
ID INT NOT NULL AUTO_INCREMENT,
constrainttimetable_ID INT NOT NULL,
constraint_type_operator_ID INT NOT NULL,
left_constraint_type_entity_ID INT NOT NULL,
right_constraint_type_entity_ID INT NOT NULL,
attribute_name VARCHAR( 250 ) NOT NULL,
PRIMARY KEY pk_enclosure_constraint ( ID ),
UNIQUE KEY uk_enclosure_constraint ( constrainttimetable_ID )
) ENGINE = InnoDB;


CREATE TABLE gap_constraint(
ID INT NOT NULL AUTO_INCREMENT,
constrainttimetable_ID INT NOT NULL,
constraint_type_entity_ID INT NOT NULL,
constraint_timeslot_type_ID INT NOT NULL,
mingap INT NOT NULL DEFAULT -1,
maxgap INT NOT NULL DEFAULT -1,
minsuc INT NOT NULL DEFAULT -1,
maxsuc INT NOT NULL DEFAULT -1,
PRIMARY KEY pk_limit_constraint ( ID ),
UNIQUE KEY uk_limit_constraint ( constrainttimetable_ID )
) ENGINE = InnoDB;

CREATE TABLE general_constraint(
ID INT NOT NULL AUTO_INCREMENT,
constrainttimetable_ID INT NOT NULL,
constraint_type_operator_ID INT NOT NULL,
left_constraint_type_entity_ID INT NOT NULL,
right_constraint_type_entity_ID INT NOT NULL,
left_attribute_name VARCHAR( 250 ) NOT NULL,
right_attribute_name VARCHAR( 250 ) NOT NULL,
constraint_attribute_type_ID INT NOT NULL,
PRIMARY KEY pk_general_constraint ( ID ),
UNIQUE KEY uk_general_constraint ( constrainttimetable_ID )
) ENGINE = InnoDB;

CREATE TABLE limit_constraint(
ID INT NOT NULL AUTO_INCREMENT,
constrainttimetable_ID INT NOT NULL,
constraint_type_entity_ID INT NOT NULL,
constraint_timeslot_type_ID INT NOT NULL,
minlimit INT NOT NULL DEFAULT -1,
maxlimit INT NOT NULL DEFAULT -1,
PRIMARY KEY pk_limit_constraint ( ID ),
UNIQUE KEY uk_limit_constraint ( constrainttimetable_ID )
) ENGINE = InnoDB;

CREATE TABLE semester(
ID INT NOT NULL AUTO_INCREMENT,
name VARCHAR ( 20 ),
PRIMARY KEY pk_semester( ID ),
UNIQUE KEY uk_semester( name )
) ENGINE = InnoDB;

CREATE TABLE specific_constraint(
ID INT NOT NULL AUTO_INCREMENT,
constrainttimetable_ID INT NOT NULL,
course_section_ID INT NOT NULL,
timeslot_ID INT NULL,
classroom_ID INT NULL,
lesson_no INT NOT NULL,
PRIMARY KEY pk_specific_constraint ( ID ),
UNIQUE KEY uk_specific_constraint ( constrainttimetable_ID )
) ENGINE = InnoDB;

CREATE TABLE student(
ID VARCHAR( 512 ) NOT NULL,
name VARCHAR( 50 ) NULL,
PRIMARY KEY pk_student ( ID )
) ENGINE = InnoDB;

CREATE TABLE student_course_section(
ID INT NOT NULL AUTO_INCREMENT,
student_ID VARCHAR( 10 ) NOT NULL,
course_section_ID INT NOT NULL,
semester_ID INT NOT NULL,
PRIMARY KEY pk_student_course_section ( ID ),
UNIQUE KEY uk_student_course_section ( student_ID, course_section_ID )
) ENGINE = InnoDB;

CREATE TABLE teacher(
ID INT NOT NULL AUTO_INCREMENT,
name VARCHAR( 50 ) NOT NULL,
PRIMARY KEY pk_teacher( ID )
) ENGINE = InnoDB;

CREATE TABLE teacher_course_section(
ID INT NOT NULL AUTO_INCREMENT,
teacher_ID INT NOT NULL,
course_section_ID INT NOT NULL,
semester_ID INT NOT NULL,
PRIMARY KEY pk_teacher_course_section ( ID )
) ENGINE = InnoDB;

CREATE TABLE timeslot(
ID INT NOT NULL AUTO_INCREMENT,
timeslot_day VARCHAR( 10 ) NOT NULL,
begintime VARCHAR( 10 ) NOT NULL,
endtime VARCHAR( 10 ) NOT NULL,
PRIMARY KEY pk_timeslot ( ID ),
UNIQUE KEY uk_timeslot( timeslot_day, begintime, endtime)
) ENGINE = InnoDB;

CREATE TABLE timeslot_semester(
ID INT NOT NULL AUTO_INCREMENT,
timeslot_ID INT NOT NULL,
semester_ID INT NOT NULL,
PRIMARY KEY pk_teacher_course_section ( ID )
) ENGINE = InnoDB;

CREATE TABLE timetable(
ID INT NOT NULL AUTO_INCREMENT,
semester_ID INT NOT NULL,
department_ID INT NOT NULL,
course_section_ID INT NOT NULL,
lesson_no INT NOT NULL,
teacher_ID INT NOT NULL,
classroom_ID INT NOT NULL,
timeslot_ID INT NOT NULL,
PRIMARY KEY pk_timetable ( ID )
) ENGINE = InnoDB;

/***************************** Insert *****************************/

INSERT INTO constraint_attribute_type (ID, name) VALUES (1, 'Integer');
INSERT INTO constraint_attribute_type (ID, name) VALUES (2, 'Double');
INSERT INTO constraint_attribute_type (ID, name) VALUES (3, 'String');
INSERT INTO constraint_attribute_type (ID, name) VALUES (4, 'Time');
INSERT INTO constraint_attribute_type (ID, name) VALUES (5, 'Day');

INSERT INTO constraint_entity (ID, name, display_name) VALUES (1, 'Student', 'Student');
INSERT INTO constraint_entity (ID, name, display_name) VALUES (2, 'Teacher', 'Teacher');
INSERT INTO constraint_entity (ID, name, display_name) VALUES (3, 'Course', 'Course');
INSERT INTO constraint_entity (ID, name, display_name) VALUES (4, 'ClassRoom', 'Class Room');
INSERT INTO constraint_entity (ID, name, display_name) VALUES (5, 'TimeSlot', 'Timeslot');

INSERT INTO constraint_timeslot_type (ID, constraint_type_ID, name) VALUES (1, 2, 'TimeSlot');
INSERT INTO constraint_timeslot_type (ID, constraint_type_ID, name) VALUES (2, 2, 'Day');
INSERT INTO constraint_timeslot_type (ID, constraint_type_ID, name) VALUES (3, 3, 'TimeSlot');
INSERT INTO constraint_timeslot_type (ID, constraint_type_ID, name) VALUES (4, 3, 'Day');
INSERT INTO constraint_timeslot_type (ID, constraint_type_ID, name) VALUES (5, 3, 'Week');

INSERT INTO constraint_type (ID, name) VALUES (1, 'Specific');
INSERT INTO constraint_type (ID, name) VALUES (2, 'Gap');
INSERT INTO constraint_type (ID, name) VALUES (3, 'Limit');
INSERT INTO constraint_type (ID, name) VALUES (4, 'General');
INSERT INTO constraint_type (ID, name) VALUES (5, 'Enclosure');

INSERT INTO constraint_type_entity (ID, constraint_type_ID, constraint_entity_ID, position) VALUES (1, 2, 1, 1);
INSERT INTO constraint_type_entity (ID, constraint_type_ID, constraint_entity_ID, position) VALUES (2, 2, 2, 1);
INSERT INTO constraint_type_entity (ID, constraint_type_ID, constraint_entity_ID, position) VALUES (3, 2, 3, 1);
INSERT INTO constraint_type_entity (ID, constraint_type_ID, constraint_entity_ID, position) VALUES (4, 2, 4, 1);

INSERT INTO constraint_type_entity (ID, constraint_type_ID, constraint_entity_ID, position) VALUES (5, 3, 1, 1);
INSERT INTO constraint_type_entity (ID, constraint_type_ID, constraint_entity_ID, position) VALUES (6, 3, 2, 1);
INSERT INTO constraint_type_entity (ID, constraint_type_ID, constraint_entity_ID, position) VALUES (7, 3, 3, 1);
INSERT INTO constraint_type_entity (ID, constraint_type_ID, constraint_entity_ID, position) VALUES (8, 3, 4, 1);

INSERT INTO constraint_type_entity (ID, constraint_type_ID, constraint_entity_ID, position) VALUES (9, 4, 2, 1);
INSERT INTO constraint_type_entity (ID, constraint_type_ID, constraint_entity_ID, position) VALUES (10, 4, 3, 1);
INSERT INTO constraint_type_entity (ID, constraint_type_ID, constraint_entity_ID, position) VALUES (11, 4, 5, 2);

INSERT INTO constraint_type_entity (ID, constraint_type_ID, constraint_entity_ID, position) VALUES (12, 5, 2, 1);
INSERT INTO constraint_type_entity (ID, constraint_type_ID, constraint_entity_ID, position) VALUES (13, 5, 3, 1);
INSERT INTO constraint_type_entity (ID, constraint_type_ID, constraint_entity_ID, position) VALUES (14, 5, 5, 2);

INSERT INTO constraint_type_operator (ID, constraint_type_ID, name, operator_sign) VALUES (1, 4, 'Equal', '=');
INSERT INTO constraint_type_operator (ID, constraint_type_ID, name, operator_sign) VALUES (2, 4, 'Not Equal', '!=');
INSERT INTO constraint_type_operator (ID, constraint_type_ID, name, operator_sign) VALUES (3, 4, 'Less Than', '<');
INSERT INTO constraint_type_operator (ID, constraint_type_ID, name, operator_sign) VALUES (4, 4, 'Less Than & Equal', '<=');
INSERT INTO constraint_type_operator (ID, constraint_type_ID, name, operator_sign) VALUES (5, 4, 'Greater Than', '>');
INSERT INTO constraint_type_operator (ID, constraint_type_ID, name, operator_sign) VALUES (6, 4, 'Greater Than & Equal', '>=');
INSERT INTO constraint_type_operator (ID, constraint_type_ID, name, operator_sign) VALUES (7, 5, 'Inclusion', '{ }');
INSERT INTO constraint_type_operator (ID, constraint_type_ID, name, operator_sign) VALUES (8, 5, 'Exclusion', '!{ }');

/******************teacher constraint start********************************/
/* 1.	No teacher can teach two or more lessons in the same timeslot. -hard constraint */
INSERT INTO constrainttimetable (ID, name, apply, ishard) VALUES(1, 'teacher timeslot', 1, 1);
INSERT INTO limit_constraint (ID, constrainttimetable_ID, constraint_type_entity_ID, constraint_timeslot_type_ID, minlimit, maxlimit) VALUES (1, 1, 6, 3, -1, 1);

/* 3.	Every teacher must work in his/her available timeslots - hard constraint */
/*INSERT INTO constrainttimetable (ID, name, apply, ishard) VALUES(2, 'teacher unavailable timeslot', 1, 1);
INSERT INTO enclosure_constraint (ID, constrainttimetable_ID, constraint_type_operator_ID, left_constraint_type_entity_ID, right_constraint_type_entity_ID, attribute_name) VALUES (1, 2, 8, 12, 14, 'unavailabletimeslots');
*/
/* 4.	Teacher should not be allocated to a timeslot he/she is not willing to work in - soft constraint */
/*NSERT INTO constrainttimetable (ID, name, apply, ishard) VALUES(3, 'teacher unwilling timeslots', 1, 0);
INSERT INTO enclosure_constraint (ID, constrainttimetable_ID, constraint_type_operator_ID, left_constraint_type_entity_ID, right_constraint_type_entity_ID, attribute_name) VALUES (2, 3, 8, 12, 14, 'unwillingtimeslots');
*/
/* 5.	Every teacher must teach no. of lessons specified in a day. - hard constraint - maximum 3 lectures in a day */
INSERT INTO constrainttimetable (ID, name, apply, ishard) VALUES(4, 'teacher day', 1, 1);
INSERT INTO limit_constraint (ID, constrainttimetable_ID, constraint_type_entity_ID, constraint_timeslot_type_ID, minlimit, maxlimit) VALUES (2, 4, 6, 4, -1, 3);

/* 6.	Every teacher should have a gap of at least one timeslot between two/given lessons - hard constraint */
/*7.	Every teacher should not have gap of more than specified timeslots in a day - hard constraint */
INSERT INTO constrainttimetable (ID, name, apply, ishard) VALUES(5, 'teacher', 1, 1);
INSERT INTO gap_constraint (ID, constrainttimetable_ID, constraint_type_entity_ID, constraint_timeslot_type_ID, mingap, maxgap, minsuc, maxsuc) VALUES (1, 5, 2, 1, 1, 3, -1, 2);
/******************teacher constraint end********************************/

/******************student constraint start********************************/
/* 1.	No class can attend two or more lessons in the same timeslot - hard constraint */
INSERT INTO constrainttimetable (ID, name, apply, ishard) VALUES(6, 'student timeslot', 1, 1);
INSERT INTO limit_constraint (ID, constrainttimetable_ID, constraint_type_entity_ID, constraint_timeslot_type_ID, minlimit, maxlimit) VALUES (3, 6, 5, 3, -1, 1);

/* 3.	Each class should have a gap of at least one timeslot between two/given lessons. - hard constraint */
/* 4.	Each class should not have a gap of more than specified timeslots in a day - hard constraint */
INSERT INTO constrainttimetable (ID, name, apply, ishard) VALUES(7, 'student', 1, 1);
INSERT INTO gap_constraint (ID, constrainttimetable_ID, constraint_type_entity_ID, constraint_timeslot_type_ID, mingap, maxgap, minsuc, maxsuc) VALUES (2, 7, 1, 1, 1, 3, -1, 2);

/* 5.	Each student should have lesson on four/given days a week - hard constraint */
INSERT INTO constrainttimetable (ID, name, apply, ishard) VALUES(8, 'student day', 1, 1);
INSERT INTO limit_constraint (ID, constrainttimetable_ID, constraint_type_entity_ID, constraint_timeslot_type_ID, minlimit, maxlimit) VALUES (4, 8, 5, 4, -1, 3);
/******************student constraint end********************************/

/******************course constraint start********************************/
/* 2.	No lesson can be taught twice or more times in a day - hard constraint */
INSERT INTO constrainttimetable (ID, name, apply, ishard) VALUES(9, 'course day', 1, 1);
INSERT INTO limit_constraint (ID, constrainttimetable_ID, constraint_type_entity_ID, constraint_timeslot_type_ID, minlimit, maxlimit) VALUES (5, 9, 7, 4, -1, 1);

/* course duration should be same as timeslot duration*/
INSERT INTO constrainttimetable (ID, name, apply, ishard) VALUES(10, 'course duration', 1, 1);
INSERT INTO general_constraint (ID, constrainttimetable_ID, constraint_type_operator_ID, left_constraint_type_entity_ID, right_constraint_type_entity_ID, left_attribute_name, right_attribute_name, constraint_attribute_type_ID ) 
VALUES (1, 10, 1, 10, 11, 'duration', 'duration', 4);
/******************course constraint end********************************/


INSERT INTO semester (ID, name) VALUES (1, 'Spring 2010');

/***************************** Foreign Keys *****************************/

ALTER TABLE classroom_department ADD FOREIGN KEY fk_classroom_department_classroom (classroom_ID) REFERENCES classroom(ID);
ALTER TABLE classroom_department ADD FOREIGN KEY fk_classroom_department_department (department_ID) REFERENCES department(ID);
ALTER TABLE classroom_department ADD FOREIGN KEY fk_classroom_department_semester (semester_ID) REFERENCES semester(ID);

ALTER TABLE constraint_multivalue_property ADD FOREIGN KEY fk_constraint_multivalue_property_left_constraint_entity (left_constraint_entity_ID) REFERENCES constraint_entity(ID);
ALTER TABLE constraint_multivalue_property ADD FOREIGN KEY fk_constraint_multivalue_property_right_constraint_entity (right_constraint_entity_ID) REFERENCES constraint_entity(ID);

ALTER TABLE constraint_property ADD FOREIGN KEY fk_constraint_property_constraint_entity (constraint_entity_ID) REFERENCES constraint_entity(ID);
ALTER TABLE constraint_property ADD FOREIGN KEY fk_constraint_property_constraint_attribute_type (constraint_attribute_type_ID) REFERENCES constraint_attribute_type(ID);

ALTER TABLE constraint_timeslot_type ADD FOREIGN KEY fk_constraint_timeslot_type_constraint_type (constraint_type_ID) REFERENCES constraint_type(ID);

ALTER TABLE constraint_type_operator ADD FOREIGN KEY fk_constraint_type_operator_constraint_type (constraint_type_ID) REFERENCES constraint_type(ID);

ALTER TABLE constraint_type_entity ADD FOREIGN KEY fk_constraint_type_entity_constraint_entity (constraint_entity_ID) REFERENCES constraint_entity(ID);
ALTER TABLE constraint_type_entity ADD FOREIGN KEY fk_constraint_type_entity_constraint_type (constraint_type_ID) REFERENCES constraint_type(ID);

ALTER TABLE course_section ADD FOREIGN KEY fk_course_section_course (course_ID) REFERENCES course(ID);
ALTER TABLE course_section ADD FOREIGN KEY fk_course_section_semester (semester_ID) REFERENCES semester(ID);
ALTER TABLE course_section ADD FOREIGN KEY fk_course_section_department (department_ID) REFERENCES department(ID);

ALTER TABLE enclosure_constraint ADD FOREIGN KEY fk_enclosure_constraint_constrainttimetable (constrainttimetable_ID) REFERENCES constrainttimetable(ID);
ALTER TABLE enclosure_constraint ADD FOREIGN KEY fk_enclosure_constraint_constraint_type_operator (constraint_type_operator_ID) REFERENCES constraint_type_operator(ID);
ALTER TABLE enclosure_constraint ADD FOREIGN KEY fk_enclosure_constraint_left_constraint_type_entity (left_constraint_type_entity_ID) REFERENCES constraint_type_entity(ID);
ALTER TABLE enclosure_constraint ADD FOREIGN KEY fk_enclosure_constraint_right_constraint_type_entity (right_constraint_type_entity_ID) REFERENCES constraint_type_entity(ID);

ALTER TABLE gap_constraint ADD FOREIGN KEY fk_gap_constraint_constrainttimetable (constrainttimetable_ID) REFERENCES constrainttimetable(ID);
ALTER TABLE gap_constraint ADD FOREIGN KEY fk_gap_constraint_constraint_type_entity (constraint_type_entity_ID) REFERENCES constraint_type_entity(ID);
ALTER TABLE gap_constraint ADD FOREIGN KEY fk_gap_constraint_constraint_timeslot_type (constraint_timeslot_type_ID) REFERENCES constraint_timeslot_type(ID);

ALTER TABLE general_constraint ADD FOREIGN KEY fk_general_constraint_constrainttimetable (constrainttimetable_ID) REFERENCES constrainttimetable(ID);
ALTER TABLE general_constraint ADD FOREIGN KEY fk_general_constraint_constraint_type_operator (constraint_type_operator_ID) REFERENCES constraint_type_operator(ID);
ALTER TABLE general_constraint ADD FOREIGN KEY fk_general_constraint_left_constraint_type_entity (left_constraint_type_entity_ID) REFERENCES constraint_type_entity(ID);
ALTER TABLE general_constraint ADD FOREIGN KEY fk_general_constraint_right_constraint_type_entity (right_constraint_type_entity_ID) REFERENCES constraint_type_entity(ID);
ALTER TABLE general_constraint ADD FOREIGN KEY fk_general_constraint_constraint_attribute_type (constraint_attribute_type_ID) REFERENCES constraint_attribute_type(ID);

ALTER TABLE limit_constraint ADD FOREIGN KEY fk_limit_constraint_constrainttimetable (constrainttimetable_ID) REFERENCES constrainttimetable(ID);
ALTER TABLE limit_constraint ADD FOREIGN KEY fk_limit_constraint_constraint_type_entity (constraint_type_entity_ID) REFERENCES constraint_type_entity(ID);
ALTER TABLE limit_constraint ADD FOREIGN KEY fk_limit_constraint_constraint_timeslot_type (constraint_timeslot_type_ID) REFERENCES constraint_timeslot_type(ID);

ALTER TABLE specific_constraint ADD FOREIGN KEY fk_specific_constraint_constrainttimetable (constrainttimetable_ID) REFERENCES constrainttimetable(ID);
ALTER TABLE specific_constraint ADD FOREIGN KEY fk_specific_constraint_course_section (course_section_ID) REFERENCES course_section(ID);
ALTER TABLE specific_constraint ADD FOREIGN KEY fk_specific_constraint_timeslot (timeslot_ID) REFERENCES timeslot(ID);
ALTER TABLE specific_constraint ADD FOREIGN KEY fk_specific_constraint_classroom (classroom_ID) REFERENCES classroom(ID);

ALTER TABLE student_course_section ADD FOREIGN KEY fk_student_course_section_student (student_ID) REFERENCES student(ID);
ALTER TABLE student_course_section ADD FOREIGN KEY fk_student_course_section_course_section (course_section_ID) REFERENCES course_section(ID);
ALTER TABLE student_course_section ADD FOREIGN KEY fk_student_course_section_semester (semester_ID) REFERENCES semester(ID);

ALTER TABLE teacher_course_section ADD FOREIGN KEY fk_teacher_course_section_teacher (teacher_ID) REFERENCES teacher(ID);
ALTER TABLE teacher_course_section ADD FOREIGN KEY fk_teacher_course_section_course_section (course_section_ID) REFERENCES course_section(ID);
ALTER TABLE teacher_course_section ADD FOREIGN KEY fk_teacher_course_section_semester (semester_ID) REFERENCES semester(ID);

ALTER TABLE timeslot_semester ADD FOREIGN KEY fk_timeslot_semester_timeslot (timeslot_ID) REFERENCES timeslot(ID);
ALTER TABLE timeslot_semester ADD FOREIGN KEY fk_timeslot_semester_semester (semester_ID) REFERENCES semester(ID);

ALTER TABLE timetable ADD FOREIGN KEY fk_timetable_classroom (classroom_ID) REFERENCES classroom(ID);
ALTER TABLE timetable ADD FOREIGN KEY fk_timetable_course_section (course_section_ID) REFERENCES course_section(ID);
ALTER TABLE timetable ADD FOREIGN KEY fk_timetable_semester (semester_ID) REFERENCES semester(ID);
ALTER TABLE timetable ADD FOREIGN KEY fk_timetable_teacher (teacher_ID) REFERENCES teacher(ID);
ALTER TABLE timetable ADD FOREIGN KEY fk_timetable_timeslot (timeslot_ID) REFERENCES timeslot(ID);
ALTER TABLE timetable ADD FOREIGN KEY fk_timetable_department (department_ID) REFERENCES department(ID);