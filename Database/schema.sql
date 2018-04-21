use sql11203736;

SET FOREIGN_KEY_CHECKS=0;

DROP TABLE Adress;

DROP TABLE Disease;

DROP TABLE Doctor;

DROP TABLE Exemption;

DROP TABLE Medicine;

DROP TABLE Patient;

DROP TABLE Prescription;

DROP TABLE Referral;

DROP TABLE Term;

DROP TABLE WorkingHours;

SET FOREIGN_KEY_CHECKS=1;

-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2017-11-15 12:34:07.325

-- tables
-- Table: Adress
CREATE TABLE Adress (
    Id integer NOT NULL AUTO_INCREMENT,
    Street varchar(50) NOT NULL,
    City varchar(50) NOT NULL,
    PostalCode varchar(10) NOT NULL,
    PatientPesel varchar(11) NOT NULL,
    CONSTRAINT Adress_pk PRIMARY KEY (Id)
);

-- Table: Disease
CREATE TABLE Disease (
    Id integer NOT NULL AUTO_INCREMENT,
    Information varchar(400) NOT NULL,
    MedicineId integer NULL,
    CONSTRAINT Disease_pk PRIMARY KEY (Id)
);

-- Table: Doctor
CREATE TABLE Doctor (
    Id integer NOT NULL AUTO_INCREMENT,
    Name varchar(30) NOT NULL,
    Surname varchar(30) NOT NULL,
    Specialization varchar(40) NOT NULL,
    PhoneNumber varchar(11) NOT NULL,
    CONSTRAINT Doctor_pk PRIMARY KEY (Id)
);

-- Table: Exemption
CREATE TABLE Exemption (
    Id integer NOT NULL AUTO_INCREMENT,
    Since date NOT NULL,
    Until date NOT NULL,
    termId integer NOT NULL,
    CONSTRAINT Exemption_pk PRIMARY KEY (Id)
);

-- Table: Medicine
CREATE TABLE Medicine (
    Id integer NOT NULL AUTO_INCREMENT,
    Name varchar(500) NOT NULL,
    Information varchar(500) NOT NULL,
    CONSTRAINT Medicine_pk PRIMARY KEY (Id)
);

-- Table: Patient
CREATE TABLE Patient (
    Pesel varchar(11) NOT NULL,
    Name varchar(30) NOT NULL,
    Surname varchar(30) NOT NULL,
    DateOfBirth date NOT NULL,
    PhoneNumber varchar(11) NOT NULL,
    CONSTRAINT Patient_pk PRIMARY KEY (Pesel)
);

-- Table: Prescription
CREATE TABLE Prescription (
    Id integer NOT NULL AUTO_INCREMENT,
    ReceivedDate date NOT NULL,
    MedicinesAmount integer NOT NULL,
    MedicineId integer NOT NULL,
    termId integer NOT NULL,
    CONSTRAINT Prescription_pk PRIMARY KEY (Id)
);

-- Table: Referral
CREATE TABLE Referral (
    Id integer NOT NULL AUTO_INCREMENT,
    Intent varchar(50) NOT NULL,
    ReceivedDate date NOT NULL,
    termId integer NOT NULL,
    CONSTRAINT Referral_pk PRIMARY KEY (Id)
);

-- Table: Term
CREATE TABLE Term (
    Id integer NOT NULL AUTO_INCREMENT,
    DoctorId integer NOT NULL,
    PatientPesel varchar(11) NOT NULL,
    TermDate datetime NOT NULL,
    Place varchar(50) NOT NULL,
    CONSTRAINT Term_pk PRIMARY KEY (Id)
);

-- Table: WorkingHours
CREATE TABLE WorkingHours (
    Id integer NOT NULL AUTO_INCREMENT,
    DoctorId integer NOT NULL,
    Place varchar(60) NOT NULL,
    StartHour datetime NOT NULL,
    EndHour datetime NOT NULL,
    IsFree smallint NOT NULL,
    CONSTRAINT WorkingHours_pk PRIMARY KEY (Id)
);

-- foreign keys
-- Reference: Adress_Patient (table: Adress)
ALTER TABLE Adress ADD CONSTRAINT Adress_Patient FOREIGN KEY Adress_Patient (PatientPesel)
    REFERENCES Patient (Pesel)
    ON DELETE CASCADE;

-- Reference: Disease_Medicine (table: Disease)
ALTER TABLE Disease ADD CONSTRAINT Disease_Medicine FOREIGN KEY Disease_Medicine (MedicineId)
    REFERENCES Medicine (Id);

-- Reference: PatientDiseaseHistory_Doctor (table: Term)
ALTER TABLE Term ADD CONSTRAINT PatientDiseaseHistory_Doctor FOREIGN KEY PatientDiseaseHistory_Doctor (DoctorId)
    REFERENCES Doctor (Id);

-- Reference: PatientDiseaseHistory_Patient (table: Term)
ALTER TABLE Term ADD CONSTRAINT PatientDiseaseHistory_Patient FOREIGN KEY PatientDiseaseHistory_Patient (PatientPesel)
    REFERENCES Patient (Pesel)
    ON DELETE CASCADE;

-- Reference: PatientDiseaseHistory_Referral (table: Referral)
ALTER TABLE Referral ADD CONSTRAINT PatientDiseaseHistory_Referral FOREIGN KEY PatientDiseaseHistory_Referral (termId)
    REFERENCES Term (Id)
    ON DELETE CASCADE;

-- Reference: PatientHistory_Exemption (table: Exemption)
ALTER TABLE Exemption ADD CONSTRAINT PatientHistory_Exemption FOREIGN KEY PatientHistory_Exemption (termId)
    REFERENCES Term (Id)
    ON DELETE CASCADE;

-- Reference: PatientHistory_Prescription (table: Prescription)
ALTER TABLE Prescription ADD CONSTRAINT PatientHistory_Prescription FOREIGN KEY PatientHistory_Prescription (termId)
    REFERENCES Term (Id)
    ON DELETE CASCADE;

-- Reference: Prescription_Medicine (table: Prescription)
ALTER TABLE Prescription ADD CONSTRAINT Prescription_Medicine FOREIGN KEY Prescription_Medicine (MedicineId)
    REFERENCES Medicine (Id);

-- Reference: WorkingHours_Doctor (table: WorkingHours)
ALTER TABLE WorkingHours ADD CONSTRAINT WorkingHours_Doctor FOREIGN KEY WorkingHours_Doctor (DoctorId)
    REFERENCES Doctor (Id)
    ON DELETE CASCADE;

-- End of file.
