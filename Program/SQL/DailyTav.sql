drop table "DailyTav" purge;

create table "DailyTav" (
  "AcDate" decimal(8, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "SelfUseFlag" varchar2(1),
  "TavBal" decimal(16, 2) default 0 not null,
  "LatestFlag" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "DailyTav" add constraint "DailyTav_PK" primary key("AcDate", "CustNo", "FacmNo");

comment on table "DailyTav" is '每日暫收款餘額檔';
comment on column "DailyTav"."AcDate" is '會計日期';
comment on column "DailyTav"."CustNo" is '借款人戶號';
comment on column "DailyTav"."FacmNo" is '額度編號';
comment on column "DailyTav"."SelfUseFlag" is '額度自用記號';
comment on column "DailyTav"."TavBal" is '暫收款餘額';
comment on column "DailyTav"."LatestFlag" is '最新記號';
comment on column "DailyTav"."CreateDate" is '建檔日期時間';
comment on column "DailyTav"."CreateEmpNo" is '建檔人員';
comment on column "DailyTav"."LastUpdate" is '最後更新日期時間';
comment on column "DailyTav"."LastUpdateEmpNo" is '最後更新人員';
