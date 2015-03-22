drop database if exists booksdb;
create database booksdb;
 
use booksdb;

create table users (
	username	varchar(20) not null primary key,
	userpass	char(32) not null,
	name		varchar(70) not null
);

create table user_roles (
	username			varchar(20) not null,
	rolename 			varchar(20) not null,
	foreign key(username) references users(username) on delete cascade,
	primary key (username, rolename)
);

create table autor (
name 		varchar(40) not null primary key
);
 
create table libros (
libroid 		int not null auto_increment primary key,
titulo  		varchar(100) not null,
lengua 			varchar(20) not null,
edicion 		varchar(20) not null,
fecha_ed 		varchar(20) not null,
fecha_imp 		varchar(20) not null,
editorial		varchar(20) not null,
autor 			varchar(40) not null,
foreign key (autor) references autor (name) on delete cascade

);


create table resenya (
resenyaid               int not null auto_increment primary key,
username 				varchar(20) not null, 
name 					varchar(20) not null,
last_modified			timestamp default current_timestamp ON UPDATE CURRENT_TIMESTAMP,
creation_timestamp		datetime not null default current_timestamp,
texto 					varchar(500) not null,
libroid 				int not null,

foreign key(username) references users(username) on delete cascade,
foreign key(libroid) references libros(libroid) on delete cascade,

unique key (username, libroid)


);


