alter table "CustMain"
add "TypeCode" decimal(1,0) default 0 not null;

comment on column "CustMain"."TypeCode" is '建檔身分別' ;
commit;