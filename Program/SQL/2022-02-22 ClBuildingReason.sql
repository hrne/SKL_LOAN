drop table "ClBuildingReason" purge;

create table "ClBuildingReason" (
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "Reason" decimal(1, 0) default 0 not null,
  "OtherReason" nvarchar2(60),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ClBuildingReason" add constraint "ClBuildingReason_PK" primary key("ClCode1", "ClCode2", "ClNo");

alter table "ClBuildingReason" add constraint "ClBuildingReason_ClBuilding_FK1" foreign key ("ClCode1", "ClCode2", "ClNo") references "ClBuilding" ("ClCode1", "ClCode2", "ClNo") on delete cascade;

create unique index "ClBuildingReason_Index1" on "ClBuildingReason"("ClCode1" asc, "ClCode2" asc, "ClNo" asc);

comment on table "ClBuildingReason" is '擔保品-建物修改原因檔';
comment on column "ClBuildingReason"."ClCode1" is '擔保品-代號1';
comment on column "ClBuildingReason"."ClCode2" is '擔保品-代號2';
comment on column "ClBuildingReason"."ClNo" is '擔保品編號';
comment on column "ClBuildingReason"."Reason" is '修改原因';
comment on column "ClBuildingReason"."OtherReason" is '其他原因';
comment on column "ClBuildingReason"."CreateDate" is '建檔日期時間';
comment on column "ClBuildingReason"."CreateEmpNo" is '建檔人員';
comment on column "ClBuildingReason"."LastUpdate" is '最後更新日期時間';
comment on column "ClBuildingReason"."LastUpdateEmpNo" is '最後更新人員';
