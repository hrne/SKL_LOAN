drop table "Ias39Loss" purge;

create table "Ias39Loss" (
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "MarkDate" decimal(8, 0) default 0 not null,
  "MarkCode" decimal(1, 0) default 0 not null,
  "MarkRsn" decimal(2, 0) default 0 not null,
  "MarkRsnDesc" nvarchar2(500),
  "LosCod" decimal(2, 0) default 0 not null,
  "StartDate" decimal(8, 0) default 0 not null,
  "EndDate" decimal(8, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "Ias39Loss" add constraint "Ias39Loss_PK" primary key("CustNo", "FacmNo", "MarkDate");

comment on table "Ias39Loss" is '特殊客觀減損狀況檔';
comment on column "Ias39Loss"."CustNo" is '戶號';
comment on column "Ias39Loss"."FacmNo" is '額度編號';
comment on column "Ias39Loss"."MarkDate" is '發生日期';
comment on column "Ias39Loss"."MarkCode" is '註記碼';
comment on column "Ias39Loss"."MarkRsn" is '原因代碼';
comment on column "Ias39Loss"."MarkRsnDesc" is '原因說明';
comment on column "Ias39Loss"."LosCod" is '客觀減損條件';
comment on column "Ias39Loss"."StartDate" is '起始日期';
comment on column "Ias39Loss"."EndDate" is '終止日期';
comment on column "Ias39Loss"."CreateDate" is '建檔日期時間';
comment on column "Ias39Loss"."CreateEmpNo" is '建檔人員';
comment on column "Ias39Loss"."LastUpdate" is '最後更新日期時間';
comment on column "Ias39Loss"."LastUpdateEmpNo" is '最後更新人員';
