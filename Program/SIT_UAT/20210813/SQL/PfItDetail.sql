drop table "PfItDetail" purge;

drop sequence "PfItDetail_SEQ";

create table "PfItDetail" (
  "LogNo" decimal(11,0) not null,
  "PerfDate" decimal(8, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "RepayType" decimal(1, 0) default 0 not null,
  "DrawdownDate" decimal(8, 0) default 0 not null,
  "ProdCode" varchar2(5),
  "PieceCode" varchar2(1),
  "CntingCode" varchar2(1),
  "DrawdownAmt" decimal(16, 2) default 0 not null,
  "UnitCode" varchar2(6),
  "DistCode" varchar2(6),
  "DeptCode" varchar2(6),
  "Introducer" nvarchar2(8),
  "UnitManager" nvarchar2(8),
  "DistManager" nvarchar2(8),
  "DeptManager" nvarchar2(8),
  "PerfCnt" decimal(5, 1) default 0 not null,
  "PerfEqAmt" decimal(16, 2) default 0 not null,
  "PerfReward" decimal(16, 2) default 0 not null,
  "PerfAmt" decimal(16, 2) default 0 not null,
  "WorkMonth" decimal(6, 0) default 0 not null,
  "WorkSeason" decimal(5, 0) default 0 not null,
  "RewardDate" decimal(8, 0) default 0 not null,
  "MediaDate" decimal(8, 0) default 0 not null,
  "MediaFg" decimal(1, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "PfItDetail" add constraint "PfItDetail_PK" primary key("LogNo");

create sequence "PfItDetail_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

create index "PfItDetail_Index1" on "PfItDetail"("WorkMonth" asc, "CustNo" asc, "FacmNo" asc, "BormNo" asc, "PerfDate" asc);

create index "PfItDetail_Index2" on "PfItDetail"("CustNo" asc, "FacmNo" asc, "BormNo" asc, "PerfDate" asc);

create index "PfItDetail_Index3" on "PfItDetail"("RewardDate" asc);

comment on table "PfItDetail" is '介紹人業績明細檔';
comment on column "PfItDetail"."LogNo" is '序號';
comment on column "PfItDetail"."PerfDate" is '業績日期';
comment on column "PfItDetail"."CustNo" is '戶號';
comment on column "PfItDetail"."FacmNo" is '額度編號';
comment on column "PfItDetail"."BormNo" is '撥款序號';
comment on column "PfItDetail"."RepayType" is '還款類別';
comment on column "PfItDetail"."DrawdownDate" is '撥款日';
comment on column "PfItDetail"."ProdCode" is '商品代碼';
comment on column "PfItDetail"."PieceCode" is '計件代碼';
comment on column "PfItDetail"."CntingCode" is '是否計件';
comment on column "PfItDetail"."DrawdownAmt" is '撥款金額/追回金額';
comment on column "PfItDetail"."UnitCode" is '單位代號';
comment on column "PfItDetail"."DistCode" is '區部代號';
comment on column "PfItDetail"."DeptCode" is '部室代號';
comment on column "PfItDetail"."Introducer" is '介紹人';
comment on column "PfItDetail"."UnitManager" is '處經理代號';
comment on column "PfItDetail"."DistManager" is '區經理代號';
comment on column "PfItDetail"."DeptManager" is '部經理代號';
comment on column "PfItDetail"."PerfCnt" is '件數';
comment on column "PfItDetail"."PerfEqAmt" is '換算業績';
comment on column "PfItDetail"."PerfReward" is '業務報酬';
comment on column "PfItDetail"."PerfAmt" is '業績金額';
comment on column "PfItDetail"."WorkMonth" is '工作月';
comment on column "PfItDetail"."WorkSeason" is '工作季';
comment on column "PfItDetail"."RewardDate" is '保費檢核日';
comment on column "PfItDetail"."MediaDate" is '產出媒體檔日期';
comment on column "PfItDetail"."MediaFg" is '產出媒體檔記號';
comment on column "PfItDetail"."CreateDate" is '建檔日期時間';
comment on column "PfItDetail"."CreateEmpNo" is '建檔人員';
comment on column "PfItDetail"."LastUpdate" is '最後更新日期時間';
comment on column "PfItDetail"."LastUpdateEmpNo" is '最後更新人員';
