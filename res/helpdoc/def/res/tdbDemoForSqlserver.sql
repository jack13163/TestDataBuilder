create database school
go
use school
go

create table teacher
(
	ID varchar(255) primary key,
	name varchar(255) not null,
	sex varchar(10) not null,
	tel varchar(255),
	homeAddress varchar(255),
)

create table class
(
	id varchar(255) primary key,
	master varchar(255) foreign key(master) REFERENCES teacher(id),
	classRome varchar(255) not null
)

create table student
(
 	ID int IDENTITY primary key,
	no varchar(255) not null, 
	name varchar(255) not null,
	sex varchar(10) not null,
	age int not null,
	tel varchar(256),
	birthday datetime,
	classId varchar(255) foreign key(classId) REFERENCES class(id),
	homeAddress varchar(255),
	count int
)
go
