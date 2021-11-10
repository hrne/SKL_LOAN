alter table "MlaundryDetail" add("ManagerCheckDate" decimal(8, 0) default 0 not null );
comment on column "MlaundryDetail"."ManagerCheckDate" is '主管覆核日期';
