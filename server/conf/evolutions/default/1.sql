# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answer (
  answer_id                     bigint auto_increment not null,
  answer_description            varchar(255),
  stimulus_resource_id          bigint,
  constraint pk_answer primary key (answer_id)
);

create table caregiver (
  caregiver_id                  bigint auto_increment not null,
  first_name                    varchar(255),
  last_name                     varchar(255),
  email                         varchar(255) not null,
  gender                        varchar(255),
  caregiverlogin_id             bigint,
  constraint uq_caregiver_email unique (email),
  constraint uq_caregiver_caregiverlogin_id unique (caregiverlogin_id),
  constraint pk_caregiver primary key (caregiver_id)
);

create table caregiverchild (
  caregiver_caregiver_id        bigint not null,
  child_child_id                bigint not null,
  constraint pk_caregiverchild primary key (caregiver_caregiver_id,child_child_id)
);

create table child (
  child_id                      bigint auto_increment not null,
  first_name                    varchar(255),
  last_name                     varchar(255),
  birth_date                    datetime(6),
  gender                        varchar(255),
  childlogin_id                 bigint,
  constraint uq_child_childlogin_id unique (childlogin_id),
  constraint pk_child primary key (child_id)
);

create table exercise (
  exercise_id                   bigint auto_increment not null,
  topic_topic_id                bigint,
  level_level_id                bigint,
  question_question_id          bigint,
  right_answer_answer_id        bigint,
  author_caregiver_id           bigint,
  constraint uq_exercise_right_answer_answer_id unique (right_answer_answer_id),
  constraint pk_exercise primary key (exercise_id)
);

create table exercise_answer (
  exercise_exercise_id          bigint not null,
  answer_answer_id              bigint not null,
  constraint pk_exercise_answer primary key (exercise_exercise_id,answer_answer_id)
);

create table level (
  level_id                      bigint auto_increment not null,
  level_description             varchar(255),
  constraint pk_level primary key (level_id)
);

create table login (
  login_id                      bigint auto_increment not null,
  auth_token                    varchar(255),
  username                      varchar(256) not null,
  sha_password                  varbinary(64) not null,
  enabled                       tinyint(1) default 0 not null,
  user_type                     integer not null,
  created_utc                   datetime(6) not null,
  constraint uq_login_username unique (username),
  constraint pk_login primary key (login_id)
);

create table question (
  question_id                   bigint auto_increment not null,
  question_description          varchar(255),
  stimulus_resource_id          bigint,
  stimulus_text                 varchar(255),
  constraint pk_question primary key (question_id)
);

create table resource (
  resource_id                   bigint auto_increment not null,
  resource_path                 varchar(255),
  resource_type_resource_type_id bigint,
  resource_area_resource_area_id bigint,
  owner_caregiver_id            bigint,
  constraint pk_resource primary key (resource_id)
);

create table resource_area (
  resource_area_id              bigint auto_increment not null,
  resource_area_description     varchar(255),
  constraint pk_resource_area primary key (resource_area_id)
);

create table resource_type (
  resource_type_id              bigint auto_increment not null,
  resource_type_description     varchar(255),
  constraint pk_resource_type primary key (resource_type_id)
);

create table topic (
  topic_id                      bigint auto_increment not null,
  topic_description             varchar(255),
  constraint pk_topic primary key (topic_id)
);

alter table answer add constraint fk_answer_stimulus_resource_id foreign key (stimulus_resource_id) references resource (resource_id) on delete restrict on update restrict;
create index ix_answer_stimulus_resource_id on answer (stimulus_resource_id);

alter table caregiver add constraint fk_caregiver_caregiverlogin_id foreign key (caregiverlogin_id) references login (login_id) on delete restrict on update restrict;

alter table caregiverchild add constraint fk_caregiverchild_caregiver foreign key (caregiver_caregiver_id) references caregiver (caregiver_id) on delete restrict on update restrict;
create index ix_caregiverchild_caregiver on caregiverchild (caregiver_caregiver_id);

alter table caregiverchild add constraint fk_caregiverchild_child foreign key (child_child_id) references child (child_id) on delete restrict on update restrict;
create index ix_caregiverchild_child on caregiverchild (child_child_id);

alter table child add constraint fk_child_childlogin_id foreign key (childlogin_id) references login (login_id) on delete restrict on update restrict;

alter table exercise add constraint fk_exercise_topic_topic_id foreign key (topic_topic_id) references topic (topic_id) on delete restrict on update restrict;
create index ix_exercise_topic_topic_id on exercise (topic_topic_id);

alter table exercise add constraint fk_exercise_level_level_id foreign key (level_level_id) references level (level_id) on delete restrict on update restrict;
create index ix_exercise_level_level_id on exercise (level_level_id);

alter table exercise add constraint fk_exercise_question_question_id foreign key (question_question_id) references question (question_id) on delete restrict on update restrict;
create index ix_exercise_question_question_id on exercise (question_question_id);

alter table exercise add constraint fk_exercise_right_answer_answer_id foreign key (right_answer_answer_id) references answer (answer_id) on delete restrict on update restrict;

alter table exercise add constraint fk_exercise_author_caregiver_id foreign key (author_caregiver_id) references caregiver (caregiver_id) on delete restrict on update restrict;
create index ix_exercise_author_caregiver_id on exercise (author_caregiver_id);

alter table exercise_answer add constraint fk_exercise_answer_exercise foreign key (exercise_exercise_id) references exercise (exercise_id) on delete restrict on update restrict;
create index ix_exercise_answer_exercise on exercise_answer (exercise_exercise_id);

alter table exercise_answer add constraint fk_exercise_answer_answer foreign key (answer_answer_id) references answer (answer_id) on delete restrict on update restrict;
create index ix_exercise_answer_answer on exercise_answer (answer_answer_id);

alter table question add constraint fk_question_stimulus_resource_id foreign key (stimulus_resource_id) references resource (resource_id) on delete restrict on update restrict;
create index ix_question_stimulus_resource_id on question (stimulus_resource_id);

alter table resource add constraint fk_resource_resource_type_resource_type_id foreign key (resource_type_resource_type_id) references resource_type (resource_type_id) on delete restrict on update restrict;
create index ix_resource_resource_type_resource_type_id on resource (resource_type_resource_type_id);

alter table resource add constraint fk_resource_resource_area_resource_area_id foreign key (resource_area_resource_area_id) references resource_area (resource_area_id) on delete restrict on update restrict;
create index ix_resource_resource_area_resource_area_id on resource (resource_area_resource_area_id);

alter table resource add constraint fk_resource_owner_caregiver_id foreign key (owner_caregiver_id) references caregiver (caregiver_id) on delete restrict on update restrict;
create index ix_resource_owner_caregiver_id on resource (owner_caregiver_id);


# --- !Downs

alter table answer drop foreign key fk_answer_stimulus_resource_id;
drop index ix_answer_stimulus_resource_id on answer;

alter table caregiver drop foreign key fk_caregiver_caregiverlogin_id;

alter table caregiverchild drop foreign key fk_caregiverchild_caregiver;
drop index ix_caregiverchild_caregiver on caregiverchild;

alter table caregiverchild drop foreign key fk_caregiverchild_child;
drop index ix_caregiverchild_child on caregiverchild;

alter table child drop foreign key fk_child_childlogin_id;

alter table exercise drop foreign key fk_exercise_topic_topic_id;
drop index ix_exercise_topic_topic_id on exercise;

alter table exercise drop foreign key fk_exercise_level_level_id;
drop index ix_exercise_level_level_id on exercise;

alter table exercise drop foreign key fk_exercise_question_question_id;
drop index ix_exercise_question_question_id on exercise;

alter table exercise drop foreign key fk_exercise_right_answer_answer_id;

alter table exercise drop foreign key fk_exercise_author_caregiver_id;
drop index ix_exercise_author_caregiver_id on exercise;

alter table exercise_answer drop foreign key fk_exercise_answer_exercise;
drop index ix_exercise_answer_exercise on exercise_answer;

alter table exercise_answer drop foreign key fk_exercise_answer_answer;
drop index ix_exercise_answer_answer on exercise_answer;

alter table question drop foreign key fk_question_stimulus_resource_id;
drop index ix_question_stimulus_resource_id on question;

alter table resource drop foreign key fk_resource_resource_type_resource_type_id;
drop index ix_resource_resource_type_resource_type_id on resource;

alter table resource drop foreign key fk_resource_resource_area_resource_area_id;
drop index ix_resource_resource_area_resource_area_id on resource;

alter table resource drop foreign key fk_resource_owner_caregiver_id;
drop index ix_resource_owner_caregiver_id on resource;

drop table if exists answer;

drop table if exists caregiver;

drop table if exists caregiverchild;

drop table if exists child;

drop table if exists exercise;

drop table if exists exercise_answer;

drop table if exists level;

drop table if exists login;

drop table if exists question;

drop table if exists resource;

drop table if exists resource_area;

drop table if exists resource_type;

drop table if exists topic;

