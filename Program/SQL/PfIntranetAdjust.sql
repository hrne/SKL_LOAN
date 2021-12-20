drop table "PfIntranetAdjust" purge;

drop sequence "PfIntranetAdjust_SEQ";

create table "PfIntranetAdjust" (
  "LogNo" decimal(11,0) not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "PerfDate" decimal(8, 0) default 0 not null,
  "WorkMonth" decimal(6, 0) default 0 not null,
  "WorkSeason" decimal(5, 0) default 0 not null,
  "Introducer" varchar2(6),
  "BsOfficer" varchar2(6),
  "PerfAmt" decimal(16, 2) default 0 not null,
  "PerfCnt" decimal(5, 1) default 0 not null,
  "UnitType" varchar2(1),
  "UnitCode" varchar2(6),
  "DistCode" varchar2(6),
  "DeptCode" varchar2(6),
  "SumAmt" decimal(16, 2) default 0 not null,
  "SumCnt" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "PfIntranetAdjust" add constraint "PfIntranetAdjust_PK" primary key("LogNo");

create sequence "PfIntranetAdjust_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

comment on table "PfIntranetAdjust" is '內網報表業績調整檔';
comment on column "PfIntranetAdjust"."LogNo" is '序號';
comment on column "PfIntranetAdjust"."CustNo" is '戶號';
comment on column "PfIntranetAdjust"."FacmNo" is '額度編號';
comment on column "PfIntranetAdjust"."BormNo" is '撥款序號';
comment on column "PfIntranetAdjust"."PerfDate" is '業績日期';
comment on column "PfIntranetAdjust"."WorkMonth" is '工作月';
comment on column "PfIntranetAdjust"."WorkSeason" is '工作季';
comment on column "PfIntranetAdjust"."Introducer" is '介紹人';
comment on column "PfIntranetAdjust"."BsOfficer" is '房貸專員';
comment on column "PfIntranetAdjust"."PerfAmt" is '業績金額';
comment on column "PfIntranetAdjust"."PerfCnt" is '業績件數';
comment on column "PfIntranetAdjust"."UnitType" is '單位類別';
comment on column "PfIntranetAdjust"."UnitCode" is '單位代號';
comment on column "PfIntranetAdjust"."DistCode" is '區部代號';
comment on column "PfIntranetAdjust"."DeptCode" is '部室代號';
comment on column "PfIntranetAdjust"."SumAmt" is '累計達成加減金額';
comment on column "PfIntranetAdjust"."SumCnt" is '累計達成加減件數';
comment on column "PfIntranetAdjust"."CreateDate" is '建檔日期時間';
comment on column "PfIntranetAdjust"."CreateEmpNo" is '建檔人員';
comment on column "PfIntranetAdjust"."LastUpdate" is '最後更新日期時間';
comment on column "PfIntranetAdjust"."LastUpdateEmpNo" is '最後更新人員';
