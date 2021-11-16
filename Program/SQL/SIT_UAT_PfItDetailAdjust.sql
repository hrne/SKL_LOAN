drop table "PfItDetailAdjust" purge;

drop sequence "PfItDetailAdjust_SEQ";

create table "PfItDetailAdjust" (
  "LogNo" decimal(11,0) not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "WorkMonth" decimal(6, 0) default 0 not null,
  "WorkSeason" decimal(5, 0) default 0 not null,
  "AdjRange" decimal(1, 0) default 0 not null,
  "AdjPerfEqAmt" decimal(16, 2) default 0 not null,
  "AdjPerfReward" decimal(16, 2) default 0 not null,
  "AdjPerfAmt" decimal(16, 2) default 0 not null,
  "AdjCntingCode" varchar2(1),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "PfItDetailAdjust" add constraint "PfItDetailAdjust_PK" primary key("LogNo");

create sequence "PfItDetailAdjust_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

create index "PfItDetailAdjust_Index1" on "PfItDetailAdjust"("CustNo" asc, "FacmNo" asc, "WorkMonth" asc);

comment on table "PfItDetailAdjust" is '介紹人業績調整檔';
comment on column "PfItDetailAdjust"."LogNo" is '序號';
comment on column "PfItDetailAdjust"."CustNo" is '戶號';
comment on column "PfItDetailAdjust"."FacmNo" is '額度編號';
comment on column "PfItDetailAdjust"."WorkMonth" is '工作月';
comment on column "PfItDetailAdjust"."WorkSeason" is '工作季';
comment on column "PfItDetailAdjust"."AdjRange" is '調整記號';
comment on column "PfItDetailAdjust"."AdjPerfEqAmt" is '調整後換算業績';
comment on column "PfItDetailAdjust"."AdjPerfReward" is '業調整後務報酬';
comment on column "PfItDetailAdjust"."AdjPerfAmt" is '調整後業績金額';
comment on column "PfItDetailAdjust"."AdjCntingCode" is '調整後是否計件';
comment on column "PfItDetailAdjust"."CreateDate" is '建檔日期時間';
comment on column "PfItDetailAdjust"."CreateEmpNo" is '建檔人員';
comment on column "PfItDetailAdjust"."LastUpdate" is '最後更新日期時間';
comment on column "PfItDetailAdjust"."LastUpdateEmpNo" is '最後更新人員';
