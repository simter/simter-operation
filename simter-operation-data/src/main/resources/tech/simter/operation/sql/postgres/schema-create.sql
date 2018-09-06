/**
 * Create table script.
 * @author RJ
 */
create table st_operation (
  id serial primary key
);
comment on table st_operation is 'Operation';
comment on column st_operation.id is 'ID';