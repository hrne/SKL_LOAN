drop table "SpecInnReCheck" purge;

create table "SpecInnReCheck" (
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "Remark" nvarchar2(300),
  "Cycle" decimal(2, 0) default 0 not null,
  "ReChkYearMonth" decimal(6, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "SpecInnReCheck" add constraint "SpecInnReCheck_PK" primary key("CustNo", "FacmNo");

comment on table "SpecInnReCheck" is '指定覆審名單檔';
comment on column "SpecInnReCheck"."CustNo" is '借款人戶號';
comment on column "SpecInnReCheck"."FacmNo" is '額度號碼';
comment on column "SpecInnReCheck"."Remark" is '備註';
comment on column "SpecInnReCheck"."Cycle" is '指定覆審週期';
comment on column "SpecInnReCheck"."ReChkYearMonth" is '覆審年月';
comment on column "SpecInnReCheck"."CreateDate" is '建檔日期時間';
comment on column "SpecInnReCheck"."CreateEmpNo" is '建檔人員';
comment on column "SpecInnReCheck"."LastUpdate" is '最後更新日期時間';
comment on column "SpecInnReCheck"."LastUpdateEmpNo" is '最後更新人員';
