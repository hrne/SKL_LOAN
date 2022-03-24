drop table "HlCusData" purge;

create table "HlCusData" (
  "HlCusNo" decimal(10, 0) default 0 not null,
  "HlCusName" nvarchar2(50),
  "ProcessDate" decimal(8, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "HlCusData" add constraint "HlCusData_PK" primary key("HlCusNo");

comment on table "HlCusData" is '借款人資料';
comment on column "HlCusData"."HlCusNo" is '借款人戶號';
comment on column "HlCusData"."HlCusName" is '客戶姓名';
comment on column "HlCusData"."ProcessDate" is '更新日期';
comment on column "HlCusData"."CreateDate" is '建檔日期時間';
comment on column "HlCusData"."CreateEmpNo" is '建檔人員';
comment on column "HlCusData"."LastUpdate" is '最後更新日期時間';
comment on column "HlCusData"."LastUpdateEmpNo" is '最後更新人員';
