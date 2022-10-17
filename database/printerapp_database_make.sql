-- makefile for mysql database for klipapi
-- by Jakub Wawak
-- kubawawak@gmail.com
-- all rights reserved
DROP TABLE IF EXISTS PRINTER_SNAP;
DROP TABLE IF EXISTS PRINTER_ELEMENT;
DROP TABLE IF EXISTS ELEMENT;
DROP TABLE IF EXISTS PRINTER;
DROP TABLE IF EXISTS APPLOG;
--creating database
CREATE DATABASE IF NOT EXISTS printapp_database;
USE klipapi_database;
--table for storing applogs
CREATE TABLE APPLOG
(
    applog_id INT PRIMARY KEY AUTO_INCREMENT,
    applog_time TIMESTAMP,
    applog_code VARCHAR(10),
    applog_desc VARCHAR(300)
);
--table for storing printer data
CREATE TABLE PRINTER
(
    printer_id INT PRIMARY KEY AUTO_INCREMENT,
    printer_name VARCHAR(100),
    printer_ip VARCHAR(50),
    printer_localization VARCHAR(250),
    printer_model VARCHAR(150),
    printer_status VARCHAR(30)
);
--table for storing printer element data
CREATE TABLE ELEMENT
(
    element_id INT PRIMARY KEY AUTO_INCREMENT,
    element_name VARCHAR(40),
    element_time TIMESTAMP,
    element_details VARCHAR(300),
    element_oid VARCHAR(100),
    element_datatype VARCHAR(10)
);
--table for storing printer elements data
CREATE TABLE PRINTER_ELEMENT
(
    printer_event_id INT PRIMARY KEY AUTO_INCREMENT,
    printer_id INT,
    printer_element_time TIMESTAMP,
    element_id INT,
    element_details TEXT

    CONSTRAINT fk_printer_1 FOREIGN KEY(printer_id) REFERENCES PRINTER(printer_id),
    CONSTRAINT fk_printer_2 FOREIGN KEY(element_id) REFERENCES ELEMENT(element_id)
);
--table for storing printer snap data
CREATE TABLE PRINTER_SNAP
(
    printer_snap_id INT PRIMARY KEY AUTO_INCREMENT,
    printer_id INT,
    printer_event1_id INT,
    printer_event2_id INT,
    printer_event3_id INT,
    printer_event4_id INT
);