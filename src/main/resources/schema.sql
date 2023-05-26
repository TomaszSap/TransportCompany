CREATE TABLE if not exists employers (
  employee_id INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  surname VARCHAR(100) NOT NULL,
  email VARCHAR(255) NOT NULL,
  mobile_number VARCHAR(9) NOT NULL,
  pesel VARCHAR(11) NOT NULL,
  pwd VARCHAR(255) NOT NULL,
  PRIMARY KEY (employee_id),
  UNIQUE (email),
  created_at TIMESTAMP NOT NULL,
  created_by varchar(50) NOT NULL,
  updated_at TIMESTAMP DEFAULT NULL,
  updated_by varchar(50) DEFAULT NULL
);
CREATE TABLE if not exists cars (
  car_id int NOT NULL AUTO_INCREMENT,
  brand VARCHAR(100) NOT NULL,
  model VARCHAR(100) NOT NULL,
  registration VARCHAR(255) NOT NULL,
  isAssigned BOOLEAN,
  created_at TIMESTAMP NOT NULL,
  created_by varchar(50) NOT NULL,
  updated_at TIMESTAMP DEFAULT NULL,
  updated_by varchar(50) DEFAULT NULL,
  PRIMARY KEY (car_id)
);
CREATE TABLE if not exists courses (
  course_id INT NOT NULL AUTO_INCREMENT,
  fromWhere VARCHAR(255),
  toWhere VARCHAR(255),
  course_type VARCHAR(255),
  distance double,
  client_id INT,
  address VARCHAR(255),
  created_at TIMESTAMP NOT NULL,
  created_by varchar(50) NOT NULL,
  updated_at TIMESTAMP DEFAULT NULL,
  updated_by varchar(50) DEFAULT NULL,
  PRIMARY KEY (course_id),
  FOREIGN KEY (client_id) REFERENCES clients(client_id)

);
CREATE TABLE if not exists clients (
  client_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  surname VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  company VARCHAR(100) NOT NULL,
  NIP VARCHAR(100) NOT NULL UNIQUE,
  invoice_id int NULL,
  created_at TIMESTAMP NOT NULL,
  created_by varchar(50) NOT NULL,
  updated_at TIMESTAMP DEFAULT NULL,
  updated_by varchar(50) DEFAULT NULL,
  CONSTRAINT email_unique UNIQUE (email),
  PRIMARY KEY (client_id)
);
CREATE TABLE if not exists roles (
  role_id int NOT NULL AUTO_INCREMENT,
  role_name varchar(50) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  created_by varchar(50) NOT NULL,
  updated_at TIMESTAMP DEFAULT NULL,
  updated_by varchar(50) DEFAULT NULL,
  PRIMARY KEY (role_id)
);
CREATE TABLE if not exists company (
company_id int NOT NULL AUTO_INCREMENT,
name varchar(255) NOT NULL,
address varchar(255) NOT NULL,
nip varchar(255) NOT NULL,
phoneNumber varchar(50) NOT NULL,
created_at TIMESTAMP NOT NULL,
created_by varchar(50) NOT NULL,
updated_at TIMESTAMP DEFAULT NULL,
updated_by varchar(50) DEFAULT NULL,
PRIMARY KEY (company_id)
);
ALTER TABLE employers
    add role_id int;
ALTER TABLE employers
    add car_id int;
ALTER TABLE employers
    add course_id int;
ALTER TABLE courses
    add employee_id int;
ALTER TABLE courses
    add clients_id int;
ALTER TABLE clients
    add course_id int;

ALTER TABLE employers
    ADD CONSTRAINT FK_ROLES_ROLE_ID FOREIGN KEY (role_id) REFERENCES roles(role_id);
ALTER TABLE employers
    ADD CONSTRAINT FK_COURSES_COURSE_ID FOREIGN KEY (course_id) REFERENCES courses(course_id);
ALTER TABLE employers
    ADD CONSTRAINT FK_CARS_CAR_ID FOREIGN KEY (car_id) REFERENCES cars(car_id);
ALTER TABLE courses
    ADD CONSTRAINT FK_EMPLOYERS_EMPLOYEE_ID FOREIGN KEY (employee_id) REFERENCES employers (employee_id);
ALTER TABLE courses
    ADD CONSTRAINT FK_CLIENTS_CLIENT_ID FOREIGN KEY(clients_id)
        REFERENCES clients(client_id);
ALTER TABLE clients
    ADD CONSTRAINT FK_COURSES_COURSE_ID FOREIGN KEY (course_id) REFERENCES courses(course_id);
INSERT INTO `roles` (`role_name`,`created_at`, `created_by`)
VALUES ('ADMIN',CURDATE(),'DBA');

INSERT INTO `roles` (`role_name`,`created_at`, `created_by`)
VALUES ('DRIVER',CURDATE(),'DBA');

INSERT INTO `roles` (`role_name`,`created_at`, `created_by`)
VALUES ('ACCOUNTANT',CURDATE(),'DBA');