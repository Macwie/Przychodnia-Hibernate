CREATE TABLE Address
(
  PatientPesel INT         NOT NULL
    PRIMARY KEY,
  Street       VARCHAR(45) NULL,
  City         VARCHAR(45) NOT NULL,
  PostalCode   VARCHAR(45) NULL,
  CONSTRAINT PatientPesel_UNIQUE
  UNIQUE (PatientPesel)
)
  ENGINE = InnoDB;

CREATE TABLE Disases_Medicines
(
  MedicineId INT NOT NULL,
  DisaseId   INT NOT NULL,
  PRIMARY KEY (MedicineId, DisaseId)
)
  ENGINE = InnoDB;

CREATE INDEX disase_idx
  ON Disases_Medicines (DisaseId);

CREATE TABLE Disease
(
  Id          INT AUTO_INCREMENT
    PRIMARY KEY,
  Information VARCHAR(45) NULL,
  Name        VARCHAR(45) NOT NULL
)
  ENGINE = InnoDB;

ALTER TABLE Disases_Medicines
  ADD CONSTRAINT disase
FOREIGN KEY (DisaseId) REFERENCES Disease (Id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

CREATE TABLE Doctor
(
  Id             INT AUTO_INCREMENT
    PRIMARY KEY,
  Name           VARCHAR(45) NOT NULL,
  LastName       VARCHAR(45) NOT NULL,
  Specialization VARCHAR(45) NULL,
  PhoneNumber    VARCHAR(45) NULL
)
  ENGINE = InnoDB;

CREATE TABLE Exemption
(
  Id        INT  NOT NULL
    PRIMARY KEY,
  SinceDate DATE NOT NULL,
  UntilDate DATE NULL
)
  ENGINE = InnoDB;

CREATE TABLE Medicine
(
  Id          INT AUTO_INCREMENT
    PRIMARY KEY,
  Name        VARCHAR(45) NOT NULL,
  Information VARCHAR(45) NULL
)
  ENGINE = InnoDB;

ALTER TABLE Disases_Medicines
  ADD CONSTRAINT medicine
FOREIGN KEY (MedicineId) REFERENCES Medicine (Id)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

CREATE TABLE Patient
(
  Pesel       INT         NOT NULL
    PRIMARY KEY,
  Name        VARCHAR(20) NOT NULL,
  LastName    VARCHAR(45) NOT NULL,
  BirthDate   VARCHAR(45) NOT NULL,
  PhoneNumber VARCHAR(45) NULL,
  Email       VARCHAR(45) NULL,
  CONSTRAINT Email_UNIQUE
  UNIQUE (Email)
)
  ENGINE = InnoDB;

ALTER TABLE Address
  ADD CONSTRAINT address_patient
FOREIGN KEY (PatientPesel) REFERENCES Patient (Pesel)
  ON UPDATE CASCADE
  ON DELETE CASCADE;

CREATE TABLE Prescription
(
  Id              INT AUTO_INCREMENT
    PRIMARY KEY,
  MedicinesAmount INT NOT NULL,
  MedicineId      INT NOT NULL,
  TermId          INT NOT NULL,
  CONSTRAINT prescription_medicine
  FOREIGN KEY (MedicineId) REFERENCES Medicine (Id)
)
  ENGINE = InnoDB;

CREATE INDEX prescription_medicine_idx
  ON Prescription (MedicineId);

CREATE INDEX prescription_term_idx
  ON Prescription (TermId);

CREATE TABLE Referral
(
  Id     INT AUTO_INCREMENT
    PRIMARY KEY,
  Intent VARCHAR(45) NOT NULL,
  TermId INT         NOT NULL
)
  ENGINE = InnoDB;

CREATE INDEX referral_term_idx
  ON Referral (TermId);

CREATE TABLE Term
(
  Id           INT AUTO_INCREMENT
    PRIMARY KEY,
  Place        VARCHAR(45) NULL,
  TermDate     DATETIME    NOT NULL,
  DoctorId     INT         NOT NULL,
  PatientPesel INT         NOT NULL,
  CONSTRAINT `term-doctor`
  FOREIGN KEY (DoctorId) REFERENCES Doctor (Id),
  CONSTRAINT `term-patient`
  FOREIGN KEY (PatientPesel) REFERENCES Patient (Pesel)
)
  ENGINE = InnoDB;

CREATE INDEX `term-doctor_idx`
  ON Term (DoctorId);

CREATE INDEX `term-patient_idx`
  ON Term (PatientPesel);

ALTER TABLE Exemption
  ADD CONSTRAINT `exemption-term`
FOREIGN KEY (Id) REFERENCES Term (Id);

ALTER TABLE Prescription
  ADD CONSTRAINT prescription_term
FOREIGN KEY (TermId) REFERENCES Term (Id);

ALTER TABLE Referral
  ADD CONSTRAINT referral_term
FOREIGN KEY (TermId) REFERENCES Term (Id);

CREATE TABLE WorkingHours
(
  Id        INT AUTO_INCREMENT
    PRIMARY KEY,
  Place     VARCHAR(45) NULL,
  StartHour DATETIME    NOT NULL,
  EndHour   DATETIME    NOT NULL,
  Free      TINYINT     NOT NULL,
  DoctorId  INT         NOT NULL,
  CONSTRAINT `hours-doctor`
  FOREIGN KEY (DoctorId) REFERENCES Doctor (Id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
)
  ENGINE = InnoDB;

CREATE INDEX `hours-doctor_idx`
  ON WorkingHours (DoctorId);

