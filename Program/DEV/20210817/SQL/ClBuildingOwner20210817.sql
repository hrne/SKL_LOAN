drop table "ClBuildingOwner" purge;

create table "ClBuildingOwner" (
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "OwnerCustUKey" varchar2(32),
  "OwnerRelCode" varchar2(2),
  "OwnerPart" decimal(10, 0) default 0 not null,
  "OwnerTotal" decimal(10, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ClBuildingOwner" add constraint "ClBuildingOwner_PK" primary key("ClCode1", "ClCode2", "ClNo", "OwnerCustUKey");

alter table "ClBuildingOwner" add constraint "ClBuildingOwner_ClBuilding_FK1" foreign key ("ClCode1", "ClCode2", "ClNo") references "ClBuilding" ("ClCode1", "ClCode2", "ClNo") on delete cascade;

create index "ClBuildingOwner_Index1" on "ClBuildingOwner"("ClCode1" asc, "ClCode2" asc, "ClNo" asc);

create index "ClBuildingOwner_Index2" on "ClBuildingOwner"("OwnerCustUKey" asc);

create index "ClBuildingOwner_Index3" on "ClBuildingOwner"("ClCode1" asc, "ClCode2" asc, "ClNo" asc, "OwnerRelCode" asc);

comment on table "ClBuildingOwner" is '擔保品-建物所有權人檔';
comment on column "ClBuildingOwner"."ClCode1" is '擔保品-代號1';
comment on column "ClBuildingOwner"."ClCode2" is '擔保品-代號2';
comment on column "ClBuildingOwner"."ClNo" is '擔保品編號';
comment on column "ClBuildingOwner"."OwnerCustUKey" is '客戶識別碼';
comment on column "ClBuildingOwner"."OwnerRelCode" is '與授信戶關係';
comment on column "ClBuildingOwner"."OwnerPart" is '持份比率(分子)';
comment on column "ClBuildingOwner"."OwnerTotal" is '持份比率(分母)';
comment on column "ClBuildingOwner"."CreateDate" is '建檔日期時間';
comment on column "ClBuildingOwner"."CreateEmpNo" is '建檔人員';
comment on column "ClBuildingOwner"."LastUpdate" is '最後更新日期時間';
comment on column "ClBuildingOwner"."LastUpdateEmpNo" is '最後更新人員';
