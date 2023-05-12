drop database if exists RestaurantDatabase;
create database RestaurantDatabase;
use RestaurantDatabase;

create table Restaurants
(
    ID             varchar(16) not null,
    ManagePassword varchar(32) not null,
    Name           varchar(64) not null,
    Address        text        not null,
    Phone          varchar(16) not null,
    HeadPic        text        not null,
    Commit         text        not null,
    AuthCode       text     default null,
    UpdateFlag     boolean  default true,
    Menu           longtext default null,
    primary key (ID)
) engine = InnoDB,
  charset = utf8;

create table CustomerAccounts
(
    ID              varchar(16) not null,
    Username        varchar(16) not null,
    Password        varchar(32) not null,
    Phone           varchar(16) not null,
    SessionAuthCode varchar(32) default null,
    primary key (ID)
) engine = InnoDB,
  charset = utf8;

create table Orders
(
    ID           varchar(32) not null,
    RestaurantID varchar(16) not null,
    Money        double      not null,
    OrderTime    datetime    not null,
    Finish       boolean default false,
    Detail       longtext    not null,
    primary key (RestaurantID, ID),
    foreign key (RestaurantID) references Restaurants (ID)
) engine = InnoDB,
  charset = utf8;