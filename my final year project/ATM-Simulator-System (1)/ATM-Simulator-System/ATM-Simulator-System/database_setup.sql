CREATE DATABASE IF NOT EXISTS bankmanagementsystem;
USE bankmanagementsystem;

CREATE TABLE IF NOT EXISTS signup (
    formno VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100),
    fname VARCHAR(100),
    dob VARCHAR(50),
    gender VARCHAR(20),
    email VARCHAR(100),
    marital VARCHAR(30),
    address VARCHAR(255),
    city VARCHAR(100),
    pincode VARCHAR(20),
    state VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS signup2 (
    formno VARCHAR(20) PRIMARY KEY,
    religion VARCHAR(50),
    category VARCHAR(50),
    income VARCHAR(50),
    education VARCHAR(100),
    occupation VARCHAR(100),
    pan VARCHAR(50),
    aadhar VARCHAR(50),
    scitizen VARCHAR(10),
    eaccount VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS signup3 (
    formno VARCHAR(20) PRIMARY KEY,
    atype VARCHAR(100),
    cardno VARCHAR(20),
    pin VARCHAR(10),
    facility VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS login (
    formno VARCHAR(20),
    cardno VARCHAR(20),
    pin VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS bank (
    pin VARCHAR(10),
    date VARCHAR(100),
    mode VARCHAR(30),
    amount VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS atm_session (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pin VARCHAR(10),
    login_time TIMESTAMP,
    logout_time TIMESTAMP
);
