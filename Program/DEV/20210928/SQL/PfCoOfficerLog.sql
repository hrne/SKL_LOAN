drop table "PfCoOfficerLog";
create table "PfCoOfficerLog" as select * from "PfCoOfficer";

alter table "PfCoOfficerLog" add "SerialNo" decimal(2, 0) default 0 not null;

alter table "PfCoOfficerLog" add constraint "PfCoOfficerLog_PK" primary key("EmpNo", "EffectiveDate", "SerialNo");

comment on table "PfCoOfficerLog" is '協辦人員等級歷程檔';
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
comment on column "PfCoOfficerLog"."SerialNo" is '序號';
comment on column "PfCoOfficerLog"."CreateDate" is '建檔日期時間';
comment on column "PfCoOfficerLog"."CreateEmpNo" is '建檔人員';
comment on column "PfCoOfficerLog"."LastUpdate" is '最後更新日期時間';
comment on column "PfCoOfficerLog"."LastUpdateEmpNo" is '最後更新人員';