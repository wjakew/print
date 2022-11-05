-- makefile for mysql database for klipapi
-- by Jakub Wawak
-- kubawawak@gmail.com
-- all rights reserved
-- creating database
CREATE DATABASE IF NOT EXISTS printapp_database;
USE printapp_database;
DROP TABLE IF EXISTS HEALTH;
DROP TABLE IF EXISTS PRINTER_SNAP;
DROP TABLE IF EXISTS PRINTER_EVENT;
DROP TABLE IF EXISTS ELEMENT;
DROP TABLE IF EXISTS TONER_DATA;
DROP TABLE IF EXISTS PRINTER;
DROP TABLE IF EXISTS JOB;
DROP TABLE IF EXISTS PRINTER_JOB;
DROP TABLE IF EXISTS APPLOG;
CREATE TABLE HEALTH
(
    database_version VARCHAR(5),
    printapp_status INT,
    printapp_admin_password VARCHAR(200),
    printapp_debug_flag INT,
    printapp_instance_name VARCHAR(200)
);
INSERT INTO HEALTH (database_version,printapp_status,printapp_admin_password,printapp_debug_flag,printapp_instance_name)
VALUES ("100",1,"21232f297a57a5a743894a0e4a801fc3",1,"Instance name");
-- table for storing applogs
CREATE TABLE APPLOG
(
    applog_id INT PRIMARY KEY AUTO_INCREMENT,
    applog_time TIMESTAMP,
    applog_code VARCHAR(100),
    applog_desc VARCHAR(300)
);
-- table for storing printer data
CREATE TABLE PRINTER
(
    printer_id INT PRIMARY KEY AUTO_INCREMENT,
    printer_name VARCHAR(100),
    printer_ip VARCHAR(50),
    printer_localization VARCHAR(250),
    printer_model VARCHAR(150),
    printer_status VARCHAR(30),
    printer_type VARCHAR(30),
    printer_format VARCHAR(10),
    printer_serialnumber VARCHAR(30),
    printer_owner VARCHAR(50),
    printer_install_date VARCHAR(50)
);
-- table for storing printer element data
CREATE TABLE ELEMENT
(
    element_id INT PRIMARY KEY AUTO_INCREMENT,
    element_name VARCHAR(40),
    element_time TIMESTAMP,
    element_details VARCHAR(300),
    element_oid VARCHAR(100),
    element_datatype VARCHAR(10)
);
-- table for storing printer elements data
CREATE TABLE PRINTER_EVENT
(
    printer_event_id INT PRIMARY KEY AUTO_INCREMENT,
    printer_id INT,
    printer_event_type VARCHAR(10),
    printer_event_time TIMESTAMP,
    element_id INT,
    element_details VARCHAR(100),

    CONSTRAINT fk_printer_1 FOREIGN KEY(printer_id) REFERENCES PRINTER(printer_id),
    CONSTRAINT fk_printer_2 FOREIGN KEY(element_id) REFERENCES ELEMENT(element_id)
);

-- table for storing toner printer data
CREATE TABLE TONER_DATA
(
    toner_data_id INT PRIMARY KEY AUTO_INCREMENT,
    printer_id INT,
    toner_data_time TIMESTAMP,
    toner_data_cyan FLOAT,
    toner_data_magenta FLOAT,
    toner_data_yellow FLOAT,
    toner_data_black FLOAT,
    toner_data_value VARCHAR(100),

    CONSTRAINT fk_toner_data_1 FOREIGN KEY(printer_id) REFERENCES PRINTER(printer_id)
);
-- table for storing printer snap data
CREATE TABLE PRINTER_SNAP
(
    printer_snap_id INT PRIMARY KEY AUTO_INCREMENT,
    printer_id INT,
    printer_snap_category VARCHAR(100),
    printer_event1_id INT,
    printer_event2_id INT,
    printer_event3_id INT,
    printer_event4_id INT
);
-- table for storing printer job update templates
CREATE TABLE PRINTER_JOB
(
    printer_job_id INT PRIMARY KEY AUTO_INCREMENT,
    printer_job_name VARCHAR(30),
    element1_id INT,
    element2_id INT,
    element3_id INT,
    element4_id INT
);
-- table for storing printer jobs and printer references
CREATE TABLE JOB
(
    job_id INT PRIMARY KEY AUTO_INCREMENT,
    printer_job_id INT,
    printer_id_list VARCHAR(40),

    CONSTRAINT fk_job_1 FOREIGN KEY(printer_job_id) REFERENCES PRINTER_JOB(printer_job_id)
);

-- preparing element data for UpdateTonerData_Scenario CMYK
-- cyan MAX 1 CURR 2
INSERT INTO ELEMENT (element_name,element_time,element_details,element_oid,element_datatype)
VALUES ("cyan_toner_max_value",NULL,"cyan toner max value",".1.3.6.1.2.1.43.11.1.1.8.1.2","integer");
INSERT INTO ELEMENT (element_name,element_time,element_details,element_oid,element_datatype)
VALUES ("cyan_toner_current_value",NULL,"cyan toner current value",".1.3.6.1.2.1.43.11.1.1.9.1.2","integer");
-- magenta MAX 3 CURR 4
INSERT INTO ELEMENT (element_name,element_time,element_details,element_oid,element_datatype)
VALUES ("magenta_toner_max_value",NULL,"magenta toner max value",".1.3.6.1.2.1.43.11.1.1.8.1.3","integer");
INSERT INTO ELEMENT (element_name,element_time,element_details,element_oid,element_datatype)
VALUES ("magenta_toner_current_value",NULL,"magenta toner current value",".1.3.6.1.2.1.43.11.1.1.9.1.3","integer");
-- yellow MAX 5 CURR 6
INSERT INTO ELEMENT (element_name,element_time,element_details,element_oid,element_datatype)
VALUES ("yellow_toner_max_value",NULL,"yellow toner max value",".1.3.6.1.2.1.43.11.1.1.8.1.4","integer");
INSERT INTO ELEMENT (element_name,element_time,element_details,element_oid,element_datatype)
VALUES ("yellow_toner_current_value",NULL,"yellow toner current value",".1.3.6.1.2.1.43.11.1.1.9.1.4","integer");
-- black MAX 7 CURR 8
INSERT INTO ELEMENT (element_name,element_time,element_details,element_oid,element_datatype)
VALUES ("black_toner_max_value",NULL,"black toner max value",".1.3.6.1.2.1.43.11.1.1.8.1.1","integer");
INSERT INTO ELEMENT (element_name,element_time,element_details,element_oid,element_datatype)
VALUES ("black_toner_current_value",NULL,"black toner current value",".1.3.6.1.2.1.43.11.1.1.9.1.1","integer");

-- preparing job for toner data update
INSERT INTO PRINTER_JOB(printer_job_name,element1_id,element2_id,element3_id,element4_id)
VALUES("get_max_toner_status",1,3,5,7);
INSERT INTO PRINTER_JOB(printer_job_name,element1_id,element2_id,element3_id,element4_id)
VALUES("get_current_toner_status",2,4,6,8);



