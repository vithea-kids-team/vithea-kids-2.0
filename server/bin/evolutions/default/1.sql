# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table caregiver (
  caregiver_id              bigint auto_increment not null,
  user_name                 varchar(255),
  password                  varbinary(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  email                     varchar(255),
  gender                    varchar(255),
  active                    tinyint(1) default 0,
  constraint pk_caregiver primary key (caregiver_id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table caregiver;

SET FOREIGN_KEY_CHECKS=1;

