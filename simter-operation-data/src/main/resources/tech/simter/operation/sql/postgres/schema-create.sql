/**
 * Create table script.
 * @author RJ
 */
create table st_operation (
  id            varchar(36)  not null  primary key,
  time          timestamptz  not null,
  type          varchar(100) not null,
  operator_id   varchar(36)  not null,
  operator_name varchar(100) not null,
  target_id     varchar(36)  not null,
  target_type   varchar(100) not null,
  target_name   varchar(100),
  title         varchar(255) not null,
  result        varchar(100),
  cluster       varchar(100),
  comment       text,
  attachments   jsonb,
  fields        jsonb
);
comment on table st_operation is 'Operation';
comment on column st_operation.attachments is '[{id, name, ext, size}, ...]';
comment on column st_operation.fields is '[{id, name, type, oldValue, newValue}, ...]';
