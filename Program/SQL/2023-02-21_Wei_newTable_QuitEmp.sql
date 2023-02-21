drop table "QuitEmp" purge;

create table "QuitEmp" (
  "EmpNo" varchar2(10),
  "QuitDate" decimal(8, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "QuitEmp" add constraint "QuitEmp_PK" primary key("EmpNo");

create index "QuitEmp_Index1" on "QuitEmp"("QuitDate" asc);

create index "QuitEmp_Index2" on "QuitEmp"("QuitDate" desc);

comment on table "QuitEmp" is '員工離職日期檔';
comment on column "QuitEmp"."EmpNo" is '員工編號';
comment on column "QuitEmp"."QuitDate" is '離職/停約日';
comment on column "QuitEmp"."CreateDate" is '建檔日期時間';
comment on column "QuitEmp"."CreateEmpNo" is '建檔人員';
comment on column "QuitEmp"."LastUpdate" is '最後更新日期時間';
comment on column "QuitEmp"."LastUpdateEmpNo" is '最後更新人員';
