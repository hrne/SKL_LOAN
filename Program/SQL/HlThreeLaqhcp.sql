drop table "HlThreeLaqhcp" purge;

create table "HlThreeLaqhcp" (
  "CalDate" decimal(8, 0) default 0 not null,
  "EmpNo" varchar2(6),
  "UnitCode" varchar2(6),
  "DistCode" varchar2(6),
  "DeptCode" varchar2(6),
  "UnitName" varchar2(20),
  "DistName" varchar2(20),
  "DeptName" varchar2(20),
  "EmpName" nvarchar2(15),
  "DepartOfficer" nvarchar2(15),
  "DirectorCode" varchar2(1),
  "GoalNum" decimal(14, 2) default 0 not null,
  "GoalAmt" decimal(14, 2) default 0 not null,
  "ActNum" decimal(14, 2) default 0 not null,
  "ActAmt" decimal(14, 2) default 0 not null,
  "ActRate" decimal(14, 2) default 0 not null,
  "TGoalNum" decimal(14, 2) default 0 not null,
  "TGoalAmt" decimal(14, 2) default 0 not null,
  "TActNum" decimal(14, 2) default 0 not null,
  "TActAmt" decimal(14, 2) default 0 not null,
  "TActRate" decimal(14, 2) default 0 not null,
  "UpNo" decimal(7, 0) default 0 not null,
  "ProcessDate" decimal(8, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "HlThreeLaqhcp" add constraint "HlThreeLaqhcp_PK" primary key("CalDate", "DeptCode", "DistCode", "UnitCode");

comment on table "HlThreeLaqhcp" is '單位、區部、部室業績累計檔';
comment on column "HlThreeLaqhcp"."CalDate" is '年月日';
comment on column "HlThreeLaqhcp"."EmpNo" is '員工代號';
comment on column "HlThreeLaqhcp"."UnitCode" is '單位代號';
comment on column "HlThreeLaqhcp"."DistCode" is '區部代號';
comment on column "HlThreeLaqhcp"."DeptCode" is '部室代號';
comment on column "HlThreeLaqhcp"."UnitName" is '單位中文';
comment on column "HlThreeLaqhcp"."DistName" is '區部中文';
comment on column "HlThreeLaqhcp"."DeptName" is '部室中文';
comment on column "HlThreeLaqhcp"."EmpName" is '員工姓名';
comment on column "HlThreeLaqhcp"."DepartOfficer" is '專員姓名';
comment on column "HlThreeLaqhcp"."DirectorCode" is '處長主任別';
comment on column "HlThreeLaqhcp"."GoalNum" is '目標件數';
comment on column "HlThreeLaqhcp"."GoalAmt" is '目標金額';
comment on column "HlThreeLaqhcp"."ActNum" is '達成件數';
comment on column "HlThreeLaqhcp"."ActAmt" is '達成金額';
comment on column "HlThreeLaqhcp"."ActRate" is '本月達成率';
comment on column "HlThreeLaqhcp"."TGoalNum" is '累計目標件數';
comment on column "HlThreeLaqhcp"."TGoalAmt" is '累計目標金額';
comment on column "HlThreeLaqhcp"."TActNum" is '累計達成件數';
comment on column "HlThreeLaqhcp"."TActAmt" is '累計達成金額';
comment on column "HlThreeLaqhcp"."TActRate" is '累計達成率';
comment on column "HlThreeLaqhcp"."UpNo" is 'UpdateIdentifier';
comment on column "HlThreeLaqhcp"."ProcessDate" is '更新日期';
comment on column "HlThreeLaqhcp"."CreateDate" is '建檔日期時間';
comment on column "HlThreeLaqhcp"."CreateEmpNo" is '建檔人員';
comment on column "HlThreeLaqhcp"."LastUpdate" is '最後更新日期時間';
comment on column "HlThreeLaqhcp"."LastUpdateEmpNo" is '最後更新人員';
