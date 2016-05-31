# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table caregiver (
  caregiver_id              bigint auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  email                     varchar(255) not null,
  gender                    varchar(255),
  caregiverlogin_id         bigint,
  constraint uq_caregiver_email unique (email),
  constraint uq_caregiver_caregiverlogin_id unique (caregiverlogin_id),
  constraint pk_caregiver primary key (caregiver_id))
;

create table child (
  child_id                  bigint auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  birth_date                date,
  gender                    varchar(255),
  childlogin_id             bigint,
  constraint uq_child_childlogin_id unique (childlogin_id),
  constraint pk_child primary key (child_id))
;

create table login (
  login_id                  bigint auto_increment not null,
  user_name                 varchar(255),
  password                  varbinary(255),
  enabled                   tinyint(1) default 0,
  user_type                 integer,
  constraint pk_login primary key (login_id))
;


create table CaregiverChild (
  caregiver_caregiver_id         bigint not null,
  login_login_id                 bigint not null,
  constraint pk_CaregiverChild primary key (caregiver_caregiver_id, login_login_id))
;
alter table caregiver add constraint fk_caregiver_caregiverLogin_1 foreign key (caregiverlogin_id) references login (login_id) on delete restrict on update restrict;
create index ix_caregiver_caregiverLogin_1 on caregiver (caregiverlogin_id);
alter table child add constraint fk_child_childLogin_2 foreign key (childlogin_id) references login (login_id) on delete restrict on update restrict;
create index ix_child_childLogin_2 on child (childlogin_id);



alter table CaregiverChild add constraint fk_CaregiverChild_caregiver_01 foreign key (caregiver_caregiver_id) references caregiver (caregiver_id) on delete restrict on update restrict;

alter table CaregiverChild add constraint fk_CaregiverChild_login_02 foreign key (login_login_id) references login (login_id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table caregiver;

drop table CaregiverChild;

drop table child;

drop table login;

SET FOREIGN_KEY_CHECKS=1;

