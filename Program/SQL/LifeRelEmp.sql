drop table "LifeRelEmp" purge;

create table "LifeRelEmp" (
  "EmpId" varchar2(10),
  "EmpName" varchar2(100),
  "LoanBalance" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "LifeRelEmp" add constraint "LifeRelEmp_PK" primary key("EmpId");

comment on table "LifeRelEmp" is '人壽利關人職員檔 T07_2
(使用報表：LM013)';
comment on column "LifeRelEmp"."EmpId" is '職員身分證/統一編號';
comment on column "LifeRelEmp"."EmpName" is '職員名稱';
comment on column "LifeRelEmp"."LoanBalance" is '放款金額';
comment on column "LifeRelEmp"."CreateDate" is '建檔日期時間';
comment on column "LifeRelEmp"."CreateEmpNo" is '建檔人員';
comment on column "LifeRelEmp"."LastUpdate" is '最後更新日期時間';
comment on column "LifeRelEmp"."LastUpdateEmpNo" is '最後更新人員';
