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
  remark        long varchar,
  items_count   smallint     not null default 0
);
create index st_operation_target_idx on st_operation(target_type, target_id);
create index st_operation_batch_idx on st_operation(batch);

create table st_operation_item (
  pid        varchar(50)  not null references st_operation on delete cascade,
  id         varchar(100) not null,
  title      varchar(100),
  value_type varchar(100) not null,
  new_value  long varchar,
  old_value  long varchar,
  primary key (pid, id)
);
