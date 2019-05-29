-- Create table script

create table st_operation (
  id            varchar(50)  not null primary key,
  ts            timestamp    not null,
  type          varchar(100) not null,
  operator_id   varchar(100) not null,
  operator_name varchar(100) not null,
  target_type   varchar(100) not null,
  target_id     varchar(100) not null,
  batch         varchar(100),
  title         varchar(100),
  result        varchar(100),
  remark        text,
  items_count   smallint     not null default 0
);
create index on st_operation(target_type, target_id);
create index on st_operation(batch);
comment on table st_operation is 'Operation';
comment on column st_operation.ts is 'operation timestamp';
comment on column st_operation.type is 'type: Creation|Modification|Deletion|Confirmation|Approval|Rejection|...';
comment on column st_operation.operator_id is 'operator id';
comment on column st_operation.operator_name is 'operator name';
comment on column st_operation.target_type is 'target type';
comment on column st_operation.target_id is 'target id';
comment on column st_operation.batch is 'belong batch';
comment on column st_operation.title is 'description title';
comment on column st_operation.result is 'operation result';
comment on column st_operation.remark is 'operation remark';
comment on column st_operation.items_count is 'operation item total count';

create table st_operation_item (
  pid        varchar(50)  not null,
  id         varchar(100) not null,
  title      varchar(100),
  value_type varchar(100) not null,
  new_value  text,
  old_value  text,
  foreign key (pid) references st_operation(id) on delete cascade,
  primary key (pid, id)
);
comment on table st_operation_item is 'operation field';
comment on column st_operation_item.pid is 'belong operation';
comment on column st_operation_item.id is 'item identifier';
comment on column st_operation_item.title is 'item title, label or name';
comment on column st_operation_item.value_type is 'value type, eg: String|JsonObject|JsonArray';
comment on column st_operation_item.new_value is 'new value';
comment on column st_operation_item.old_value is 'old value';
