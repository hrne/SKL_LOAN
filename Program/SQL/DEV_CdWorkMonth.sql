drop table "CdWorkMonth" purge;

create table "CdWorkMonth" (
  "Year" decimal(4, 0) default 0 not null,
  "Month" decimal(2, 0) default 0 not null,
  "StartDate" decimal(8, 0) default 0 not null,
  "EndDate" decimal(8, 0) default 0 not null,
  "BonusDate" decimal(8, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdWorkMonth" add constraint "CdWorkMonth_PK" primary key("Year", "Month");

comment on table "CdWorkMonth" is '放款業績工作月對照檔';
comment on column "CdWorkMonth"."Year" is '業績年度';
comment on column "CdWorkMonth"."Month" is '工作月份';
comment on column "CdWorkMonth"."StartDate" is '開始日期';
comment on column "CdWorkMonth"."EndDate" is '終止日期';
comment on column "CdWorkMonth"."BonusDate" is '獎金發放日';
comment on column "CdWorkMonth"."CreateDate" is '建檔日期時間';
comment on column "CdWorkMonth"."CreateEmpNo" is '建檔人員';
comment on column "CdWorkMonth"."LastUpdate" is '最後更新日期時間';
comment on column "CdWorkMonth"."LastUpdateEmpNo" is '最後更新人員';
