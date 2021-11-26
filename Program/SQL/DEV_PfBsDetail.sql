drop table "PfBsDetail" purge;

drop sequence "PfBsDetail_SEQ";

create table "PfBsDetail" (
  "LogNo" decimal(11,0) not null,
  "PerfDate" decimal(8, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "RepayType" decimal(1, 0) default 0 not null,
  "BsOfficer" varchar2(6),
  "DeptCode" varchar2(6),
  "DrawdownDate" decimal(8, 0) default 0 not null,
  "ProdCode" varchar2(5),
  "PieceCode" varchar2(1),
  "DrawdownAmt" decimal(16, 2) default 0 not null,
  "PerfCnt" decimal(5, 1) default 0 not null,
  "PerfAmt" decimal(16, 2) default 0 not null,
  "AdjPerfCnt" decimal(5, 1) default 0 not null,
  "AdjPerfAmt" decimal(16, 2) default 0 not null,
  "WorkMonth" decimal(6, 0) default 0 not null,
  "WorkSeason" decimal(5, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "PfBsDetail" add constraint "PfBsDetail_PK" primary key("LogNo");

create sequence "PfBsDetail_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

create index "PfBsDetail_Index1" on "PfBsDetail"("WorkMonth" asc, "CustNo" asc, "FacmNo" asc, "BormNo" asc, "PerfDate" asc);

create index "PfBsDetail_Index2" on "PfBsDetail"("CustNo" asc, "FacmNo" asc, "BormNo" asc, "PerfDate" asc);

comment on table "PfBsDetail" is '房貸專員業績明細檔';
comment on column "PfBsDetail"."LogNo" is '序號';
comment on column "PfBsDetail"."PerfDate" is '業績日期';
comment on column "PfBsDetail"."CustNo" is '戶號';
comment on column "PfBsDetail"."FacmNo" is '額度編號';
comment on column "PfBsDetail"."BormNo" is '撥款序號';
comment on column "PfBsDetail"."RepayType" is '還款類別';
comment on column "PfBsDetail"."BsOfficer" is '房貸專員';
comment on column "PfBsDetail"."DeptCode" is '部室代號';
comment on column "PfBsDetail"."DrawdownDate" is '撥款日';
comment on column "PfBsDetail"."ProdCode" is '商品代碼';
comment on column "PfBsDetail"."PieceCode" is '計件代碼';
comment on column "PfBsDetail"."DrawdownAmt" is '撥款金額/追回金額';
comment on column "PfBsDetail"."PerfCnt" is '件數';
comment on column "PfBsDetail"."PerfAmt" is '業績金額';
comment on column "PfBsDetail"."AdjPerfCnt" is '週整加減件數';
comment on column "PfBsDetail"."AdjPerfAmt" is '週整加減業績金額';
comment on column "PfBsDetail"."WorkMonth" is '工作月';
comment on column "PfBsDetail"."WorkSeason" is '工作季';
comment on column "PfBsDetail"."CreateDate" is '建檔日期時間';
comment on column "PfBsDetail"."CreateEmpNo" is '建檔人員';
comment on column "PfBsDetail"."LastUpdate" is '最後更新日期時間';
comment on column "PfBsDetail"."LastUpdateEmpNo" is '最後更新人員';
