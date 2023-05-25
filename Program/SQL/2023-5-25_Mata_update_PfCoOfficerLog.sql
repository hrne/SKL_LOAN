drop table "PfCoOfficerLog" purge;

drop sequence "PfCoOfficerLog_SEQ";

create table "PfCoOfficerLog" (
  "LogNo" decimal(11,0) not null,
  "EmpNo" varchar2(6),
  "EffectiveDate" decimal(8, 0) default 0 not null,
  "IneffectiveDate" decimal(8, 0) default 0 not null,
  "AreaCode" varchar2(6),
  "DistCode" varchar2(6),
  "DeptCode" varchar2(6),
  "AreaItem" varchar2(20),
  "DistItem" varchar2(20),
  "DeptItem" varchar2(20),
  "EmpClass" varchar2(1),
  "ClassPass" varchar2(1),
  "UpdateTlrNo" varchar2(6),
  "UpdateDate" timestamp,
  "FunctionCode" decimal(1, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "PfCoOfficerLog" add constraint "PfCoOfficerLog_PK" primary key("LogNo");

create sequence "PfCoOfficerLog_SEQ" minvalue 1 maxvalue 2147483647 increment by 1 start with 1 nocache cycle;

comment on table "PfCoOfficerLog" is '協辦人員等級歷程檔';
comment on column "PfCoOfficerLog"."LogNo" is '序號';
comment on column "PfCoOfficerLog"."EmpNo" is '員工代號';
comment on column "PfCoOfficerLog"."EffectiveDate" is '生效日期';
comment on column "PfCoOfficerLog"."IneffectiveDate" is '停效日期';
comment on column "PfCoOfficerLog"."AreaCode" is '單位代號';
comment on column "PfCoOfficerLog"."DistCode" is '區部代號';
comment on column "PfCoOfficerLog"."DeptCode" is '部室代號';
comment on column "PfCoOfficerLog"."AreaItem" is '單位中文';
comment on column "PfCoOfficerLog"."DistItem" is '區部中文';
comment on column "PfCoOfficerLog"."DeptItem" is '部室中文';
comment on column "PfCoOfficerLog"."EmpClass" is '協辦等級';
comment on column "PfCoOfficerLog"."ClassPass" is '初階授信通過';
comment on column "PfCoOfficerLog"."UpdateTlrNo" is '更新經辦';
comment on column "PfCoOfficerLog"."UpdateDate" is '更新日期時間';
comment on column "PfCoOfficerLog"."FunctionCode" is '功能';
comment on column "PfCoOfficerLog"."CreateDate" is '建檔日期時間';
comment on column "PfCoOfficerLog"."CreateEmpNo" is '建檔人員';
comment on column "PfCoOfficerLog"."LastUpdate" is '最後更新日期時間';
comment on column "PfCoOfficerLog"."LastUpdateEmpNo" is '最後更新人員';
