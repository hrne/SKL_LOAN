drop table "HlEmpLnYg5Pt" purge;

create table "HlEmpLnYg5Pt" (
  "WorkYM" decimal(6, 0) default 0 not null,
  "EmpNo" varchar2(6),
  "Fullname" varchar2(40),
  "AreaCode" varchar2(6),
  "AreaItem" nvarchar2(12),
  "DeptCode" varchar2(6),
  "DepItem" nvarchar2(12),
  "DistCode" varchar2(6),
  "DistItem" nvarchar2(30),
  "StationName" nvarchar2(30),
  "GoalAmt" decimal(14, 2) default 0 not null,
  "HlAppNum" decimal(14, 2) default 0 not null,
  "HlAppAmt" decimal(14, 2) default 0 not null,
  "ClAppNum" decimal(14, 2) default 0 not null,
  "ClAppAmt" decimal(14, 2) default 0 not null,
  "ServiceAppNum" decimal(14, 2) default 0 not null,
  "ServiceAppAmt" decimal(14, 2) default 0 not null,
  "CalDate" decimal(8, 0) default 0 not null,
  "UpNo" decimal(7, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "HlEmpLnYg5Pt" add constraint "HlEmpLnYg5Pt_PK" primary key("WorkYM", "EmpNo");

create index "HlEmpLnYg5Pt_Index1" on "HlEmpLnYg5Pt"("CalDate" asc);

comment on table "HlEmpLnYg5Pt" is '房貨專員目標檔案';
comment on column "HlEmpLnYg5Pt"."WorkYM" is '年月份';
comment on column "HlEmpLnYg5Pt"."EmpNo" is '員工代號';
comment on column "HlEmpLnYg5Pt"."Fullname" is '員工姓名';
comment on column "HlEmpLnYg5Pt"."AreaCode" is '區域中心';
comment on column "HlEmpLnYg5Pt"."AreaItem" is '中心中文';
comment on column "HlEmpLnYg5Pt"."DeptCode" is '部室代號';
comment on column "HlEmpLnYg5Pt"."DepItem" is '部室中文';
comment on column "HlEmpLnYg5Pt"."DistCode" is '區部代號';
comment on column "HlEmpLnYg5Pt"."DistItem" is '區部中文';
comment on column "HlEmpLnYg5Pt"."StationName" is '駐在地';
comment on column "HlEmpLnYg5Pt"."GoalAmt" is '目標金額';
comment on column "HlEmpLnYg5Pt"."HlAppNum" is '房貸撥款件數';
comment on column "HlEmpLnYg5Pt"."HlAppAmt" is '房貸撥款金額';
comment on column "HlEmpLnYg5Pt"."ClAppNum" is '車貸撥款件數';
comment on column "HlEmpLnYg5Pt"."ClAppAmt" is '車貸撥款金額';
comment on column "HlEmpLnYg5Pt"."ServiceAppNum" is '信義撥款件數';
comment on column "HlEmpLnYg5Pt"."ServiceAppAmt" is '信義撥款金額';
comment on column "HlEmpLnYg5Pt"."CalDate" is '資料日期';
comment on column "HlEmpLnYg5Pt"."UpNo" is 'UpdateIdentifier';
comment on column "HlEmpLnYg5Pt"."CreateDate" is '建檔日期時間';
comment on column "HlEmpLnYg5Pt"."CreateEmpNo" is '建檔人員';
comment on column "HlEmpLnYg5Pt"."LastUpdate" is '最後更新日期時間';
comment on column "HlEmpLnYg5Pt"."LastUpdateEmpNo" is '最後更新人員';
