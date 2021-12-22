drop table "PfBsDetailAdjust" purge;

drop sequence "PfBsDetailAdjust_SEQ";

create table "PfBsDetailAdjust" (
  "LogNo" decimal(11,0) not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "WorkMonth" decimal(6, 0) default 0 not null,
  "WorkSeason" decimal(5, 0) default 0 not null,
  "AdjPerfCnt" decimal(5, 1) default 0 not null,
  "AdjPerfAmt" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "PfBsDetailAdjust" add constraint "PfBsDetailAdjust_PK" primary key("LogNo");

create sequence "PfBsDetailAdjust_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

create index "PfBsDetailAdjust_Index1" on "PfBsDetailAdjust"("CustNo" asc, "FacmNo" asc, "BormNo" asc);

comment on table "PfBsDetailAdjust" is '房貸專員業績調整檔';
comment on column "PfBsDetailAdjust"."LogNo" is '序號';
comment on column "PfBsDetailAdjust"."CustNo" is '戶號';
comment on column "PfBsDetailAdjust"."FacmNo" is '額度編號';
comment on column "PfBsDetailAdjust"."BormNo" is '撥款序號';
comment on column "PfBsDetailAdjust"."WorkMonth" is '工作月';
comment on column "PfBsDetailAdjust"."WorkSeason" is '工作季';
comment on column "PfBsDetailAdjust"."AdjPerfCnt" is '週整後件數';
comment on column "PfBsDetailAdjust"."AdjPerfAmt" is '週整後業績金額';
comment on column "PfBsDetailAdjust"."CreateDate" is '建檔日期時間';
comment on column "PfBsDetailAdjust"."CreateEmpNo" is '建檔人員';
comment on column "PfBsDetailAdjust"."LastUpdate" is '最後更新日期時間';
comment on column "PfBsDetailAdjust"."LastUpdateEmpNo" is '最後更新人員';
