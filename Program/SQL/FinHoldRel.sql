drop table "FinHoldRel" purge;

create table "FinHoldRel" (
  "CompanyName" varchar2(100),
  "Id" varchar2(10),
  "Name" varchar2(100),
  "BusTitle" varchar2(100),
  "LineAmt" decimal(16, 2) default 0 not null,
  "LoanBalance" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "FinHoldRel" add constraint "FinHoldRel_PK" primary key("Id");

comment on table "FinHoldRel" is '金控利關人名單檔 T044
(使用報表：LM049、LQ005)';
comment on column "FinHoldRel"."CompanyName" is '所在公司';
comment on column "FinHoldRel"."Id" is '身分證/統一編號';
comment on column "FinHoldRel"."Name" is '姓名';
comment on column "FinHoldRel"."BusTitle" is '職務';
comment on column "FinHoldRel"."LineAmt" is '核貸金額';
comment on column "FinHoldRel"."LoanBalance" is '放款金額';
comment on column "FinHoldRel"."CreateDate" is '建檔日期時間';
comment on column "FinHoldRel"."CreateEmpNo" is '建檔人員';
comment on column "FinHoldRel"."LastUpdate" is '最後更新日期時間';
comment on column "FinHoldRel"."LastUpdateEmpNo" is '最後更新人員';
