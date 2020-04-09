create table st_operation (
  id            varchar(50)  not null primary key,
  ts            timestamp    not null comment 'operation timestamp',
  type          varchar(100) not null comment 'type: Creation|Modification|Deletion|Confirmation|Approval|Rejection|...',
  operator_id   varchar(100) not null comment 'operator id',
  operator_name varchar(100) not null comment 'operator name',
  target_type   varchar(100) not null comment 'target type',
  target_id     varchar(100) not null comment 'target id',
  batch         varchar(100) comment 'belong batch',
  title         varchar(100) comment 'description title',
  result        varchar(100) comment 'operation result',
  remark        text comment 'operation remark',
  items_count   tinyint      not null default 0 comment 'operation item total count',
  index (target_type, target_id),
  index (batch)
) comment = 'Operation';

create table st_operation_item (
  pid        varchar(50)  not null comment 'belong operation',
  id         varchar(100) not null comment 'item identifier',
  title      varchar(100) comment 'item title, label or name',
  value_type varchar(100) not null comment 'value type, eg: String|JsonObject|JsonArray',
  new_value  text comment 'new value',
  old_value  text comment 'old value',
  primary key (pid, id),
  foreign key (pid) references st_operation(id) on delete cascade
) comment = 'operation item';
