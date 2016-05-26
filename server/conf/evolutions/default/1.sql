# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table caregiver (
  caregiver_id              bigint auto_increment not null,
  user_name                 varchar(255),
  password                  varbinary(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  email                     varchar(255) not null,
  gender                    varchar(255),
  active                    tinyint(1) default 0,
  constraint uq_caregiver_email unique (email),
  constraint pk_caregiver primary key (caregiver_id))
;

create table child (
  id                        bigint auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  birth_date                date,
  gender                    varchar(255),
  child_id                  bigint,
  constraint uq_child_child_id unique (child_id),
  constraint pk_child primary key (id))
;

create table child_login (
  child_id                  bigint auto_increment not null,
  child_user_name           varchar(255),
  password                  varchar(255),
  enabled                   tinyint(1) default 0,
  constraint pk_child_login primary key (child_id))
;

alter table child add constraint fk_child_childLogin_1 foreign key (child_id) references child_login (child_id) on delete restrict on update restrict;
create index ix_child_childLogin_1 on child (child_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table caregiver;

drop table child;

drop table child_login;

SET FOREIGN_KEY_CHECKS=1;

