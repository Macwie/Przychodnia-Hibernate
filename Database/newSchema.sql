create table Address
(
	PatientPesel int not null
		primary key,
	Street varchar(45) null,
	City varchar(45) not null,
	PostalCode varchar(45) null,
	constraint PatientPesel_UNIQUE
		unique (PatientPesel)
)
engine=InnoDB
;

create table Disases_Medicines
(
	MedicineId int not null,
	DisaseId int not null,
	primary key (MedicineId, DisaseId)
)
engine=InnoDB
;

create index disase_idx
	on Disases_Medicines (DisaseId)
;

create table Disease
(
	Id int auto_increment
		primary key,
	Information varchar(45) null,
	Name varchar(45) not null
)
engine=InnoDB
;

alter table Disases_Medicines
	add constraint disase
		foreign key (DisaseId) references Disease (Id)
			on update cascade on delete cascade
;

create table Doctor
(
	Id int auto_increment
		primary key,
	Name varchar(45) not null,
	LastName varchar(45) not null,
	Specialization varchar(45) null,
	PhoneNumber varchar(45) null
)
engine=InnoDB
;

create table Exemption
(
	Id int not null
		primary key,
	SinceDate date not null,
	UntilDate date null
)
engine=InnoDB
;

create table Medicine
(
	Id int auto_increment
		primary key,
	Name varchar(45) not null,
	Information varchar(45) null
)
engine=InnoDB
;

alter table Disases_Medicines
	add constraint medicine
		foreign key (MedicineId) references Medicine (Id)
			on update cascade on delete cascade
;

create table Patient
(
	Pesel int not null
		primary key,
	Name varchar(20) not null,
	LastName varchar(45) not null,
	BirthDate varchar(45) not null,
	PhoneNumber varchar(45) null,
	Email varchar(45) null,
	constraint Email_UNIQUE
		unique (Email)
)
engine=InnoDB
;

alter table Address
	add constraint address_patient
		foreign key (PatientPesel) references Patient (Pesel)
			on update cascade on delete cascade
;

create table Prescription
(
	Id int auto_increment
		primary key,
	MedicinesAmount int not null,
	MedicineId int not null,
	TermId int not null,
	constraint prescription_medicine
		foreign key (MedicineId) references Medicine (Id)
)
engine=InnoDB
;

create index prescription_medicine_idx
	on Prescription (MedicineId)
;

create index prescription_term_idx
	on Prescription (TermId)
;

create table Referral
(
	Id int auto_increment
		primary key,
	Intent varchar(45) not null,
	TermId int not null
)
engine=InnoDB
;

create index referral_term_idx
	on Referral (TermId)
;

create table Term
(
	Id int auto_increment
		primary key,
	Place varchar(45) null,
	TermDate datetime not null,
	DoctorId int not null,
	PatientPesel int not null,
	constraint `term-doctor`
		foreign key (DoctorId) references Doctor (Id),
	constraint `term-patient`
		foreign key (PatientPesel) references Patient (Pesel)
)
engine=InnoDB
;

create index `term-doctor_idx`
	on Term (DoctorId)
;

create index `term-patient_idx`
	on Term (PatientPesel)
;

alter table Exemption
	add constraint `exemption-term`
		foreign key (Id) references Term (Id)
;

alter table Prescription
	add constraint prescription_term
		foreign key (TermId) references Term (Id)
;

alter table Referral
	add constraint referral_term
		foreign key (TermId) references Term (Id)
;

create table WorkingHours
(
	Id int auto_increment
		primary key,
	Place varchar(45) null,
	StartHour datetime not null,
	EndHour datetime not null,
	Free tinyint not null,
	DoctorId int not null,
	constraint `hours-doctor`
		foreign key (DoctorId) references Doctor (Id)
			on update cascade on delete cascade
)
engine=InnoDB
;

create index `hours-doctor_idx`
	on WorkingHours (DoctorId)
;

