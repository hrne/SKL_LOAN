drop table "CoreAcMain" purge;

create table "CoreAcMain" (
  "AcBookCode" varchar2(3),
  "AcSubBookCode" varchar2(3),
  "CurrencyCode" varchar2(3),
  "AcNoCode" varchar2(11),
  "AcNoName" varchar2(100),
  "AcSubCode" varchar2(5),
  "AcDate" decimal(8, 0) default 0 not null,
  "YdBal" decimal(16, 2) default 0 not null,
  "TdBal" decimal(16, 2) default 0 not null,
  "DbAmt" decimal(16, 2) default 0 not null,
  "CrAmt" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CoreAcMain" add constraint "CoreAcMain_PK" primary key("AcBookCode", "AcSubBookCode", "CurrencyCode", "AcNoCode", "AcSubCode", "AcDate");

comment on table "CoreAcMain" is '核心會計總帳檔';
comment on column "CoreAcMain"."AcBookCode" is '帳冊別';
comment on column "CoreAcMain"."AcSubBookCode" is '區隔帳冊';
comment on column "CoreAcMain"."CurrencyCode" is '幣別';
comment on column "CoreAcMain"."AcNoCode" is '科目代號';
comment on column "CoreAcMain"."AcNoName" is '科目名稱';
comment on column "CoreAcMain"."AcSubCode" is '子目代號';
comment on column "CoreAcMain"."AcDate" is '會計日期';
comment on column "CoreAcMain"."YdBal" is '前日餘額';
comment on column "CoreAcMain"."TdBal" is '本日餘額';
comment on column "CoreAcMain"."DbAmt" is '借方金額';
comment on column "CoreAcMain"."CrAmt" is '貸方金額';
comment on column "CoreAcMain"."CreateDate" is '建檔日期時間';
comment on column "CoreAcMain"."CreateEmpNo" is '建檔人員';
comment on column "CoreAcMain"."LastUpdate" is '最後更新日期時間';
comment on column "CoreAcMain"."LastUpdateEmpNo" is '最後更新人員';
