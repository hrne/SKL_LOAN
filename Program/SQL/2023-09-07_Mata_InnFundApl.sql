drop table "InnFundApl" purge;

create table "InnFundApl" (
  "AcDate" decimal(8, 0) default 0 not null,
  "ResrvStndrd" decimal(14, 0) default 0 not null,
  "PosbleBorPsn" decimal(7, 4) default 0 not null,
  "PosbleBorAmt" decimal(16, 2) default 0 not null,
  "AlrdyBorAmt" decimal(16, 2) default 0 not null,
  "StockHoldersEqt" decimal(16, 2) default 0 not null,
  "AvailableFunds" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "InnFundApl" add constraint "InnFundApl_PK" primary key("AcDate");

comment on table "InnFundApl" is '資金運用概況檔';
comment on column "InnFundApl"."AcDate" is '日期';
comment on column "InnFundApl"."ResrvStndrd" is '責任準備金';
comment on column "InnFundApl"."PosbleBorPsn" is '可放款比率%';
comment on column "InnFundApl"."PosbleBorAmt" is '可放款金額';
comment on column "InnFundApl"."AlrdyBorAmt" is '已放款金額';
comment on column "InnFundApl"."StockHoldersEqt" is '股東權益';
comment on column "InnFundApl"."AvailableFunds" is '可運用資金';
comment on column "InnFundApl"."CreateDate" is '建檔日期時間';
comment on column "InnFundApl"."CreateEmpNo" is '建檔人員';
comment on column "InnFundApl"."LastUpdate" is '最後更新日期時間';
comment on column "InnFundApl"."LastUpdateEmpNo" is '最後更新人員';
