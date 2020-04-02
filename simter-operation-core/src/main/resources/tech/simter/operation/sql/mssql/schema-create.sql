create table st_operation (
  id            varchar(50)    not null,
  ts            datetimeoffset not null,
  type          varchar(100)   not null,
  operator_id   varchar(100)   not null,
  operator_name varchar(100)   not null,
  target_type   varchar(100)   not null,
  target_id     varchar(100)   not null,
  batch         varchar(100),
  title         varchar(100),
  result        varchar(100),
  remark        varchar(max),
  items_count   smallint       not null default 0,
  primary key (id)
);
create index on st_operation(target_type, target_id);
create index on st_operation(batch);

create table st_operation_item (
  pid        varchar(50)  not null,
  id         varchar(100) not null,
  title      varchar(100),
  value_type varchar(100) not null,
  new_value  varchar(max),
  old_value  varchar(max),
  primary key (pid, id),
  foreign key (pid) references st_operation(id) on delete cascade
);
