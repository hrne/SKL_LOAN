drop table "PfRewardMedia" purge;

drop sequence "PfRewardMedia_SEQ";

create table "PfRewardMedia" (
  "BonusNo" decimal(11,0) not null,
  "BonusDate" decimal(8, 0) default 0 not null,
  "PerfDate" decimal(8, 0) default 0 not null,
  "CustNo" decimal(7, 0) default 0 not null,
  "FacmNo" decimal(3, 0) default 0 not null,
  "BormNo" decimal(3, 0) default 0 not null,
  "BonusType" decimal(1, 0) default 0 not null,
  "EmployeeNo" varchar2(6),
  "ProdCode" varchar2(5),
  "PieceCode" varchar2(1),
  "Bonus" decimal(14, 2) default 0 not null,
  "AdjustBonus" decimal(14, 2) default 0 not null,
  "AdjustBonusDate" decimal(8, 0) default 0 not null,
  "WorkMonth" decimal(6, 0) default 0 not null,
  "WorkSeason" decimal(5, 0) default 0 not null,
  "Remark" nvarchar2(50),
  "MediaFg" decimal(1, 0) default 0 not null,
  "MediaDate" decimal(8, 0) default 0 not null,
  "ManualFg" decimal(1, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "PfRewardMedia" add constraint "PfRewardMedia_PK" primary key("BonusNo");

create sequence "PfRewardMedia_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

create index "PfRewardMedia_Index1" on "PfRewardMedia"("WorkMonth" asc, "BonusNo" asc);

create index "PfRewardMedia_Index2" on "PfRewardMedia"("CustNo" asc, "FacmNo" asc, "BormNo" asc);

comment on table "PfRewardMedia" is '獎金媒體發放檔';
comment on column "PfRewardMedia"."BonusNo" is '系統序號';
comment on column "PfRewardMedia"."BonusDate" is '獎金發放日';
comment on column "PfRewardMedia"."PerfDate" is '業績日期';
comment on column "PfRewardMedia"."CustNo" is '戶號';
comment on column "PfRewardMedia"."FacmNo" is '額度編號';
comment on column "PfRewardMedia"."BormNo" is '撥款序號';
comment on column "PfRewardMedia"."BonusType" is '獎金類別';
comment on column "PfRewardMedia"."EmployeeNo" is '獎金發放員工編號';
comment on column "PfRewardMedia"."ProdCode" is '商品代碼';
comment on column "PfRewardMedia"."PieceCode" is '計件代碼';
comment on column "PfRewardMedia"."Bonus" is '原始獎金';
comment on column "PfRewardMedia"."AdjustBonus" is '發放獎金';
comment on column "PfRewardMedia"."AdjustBonusDate" is '調整獎金日期';
comment on column "PfRewardMedia"."WorkMonth" is '工作月';
comment on column "PfRewardMedia"."WorkSeason" is '工作季';
comment on column "PfRewardMedia"."Remark" is '備註';
comment on column "PfRewardMedia"."MediaFg" is '產出媒體檔記號';
comment on column "PfRewardMedia"."MediaDate" is '產出媒體檔日期';
comment on column "PfRewardMedia"."ManualFg" is '人工新增記號';
comment on column "PfRewardMedia"."CreateDate" is '建檔日期時間';
comment on column "PfRewardMedia"."CreateEmpNo" is '建檔人員';
comment on column "PfRewardMedia"."LastUpdate" is '最後更新日期時間';
comment on column "PfRewardMedia"."LastUpdateEmpNo" is '最後更新人員';
