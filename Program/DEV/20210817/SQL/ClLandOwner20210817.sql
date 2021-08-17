drop table "ClLandOwner" purge;

create table "ClLandOwner" (
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "LandSeq" decimal(3, 0) default 0 not null,
  "OwnerCustUKey" varchar2(32),
  "OwnerRelCode" varchar2(2),
  "OwnerPart" decimal(10, 0) default 0 not null,
  "OwnerTotal" decimal(10, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ClLandOwner" add constraint "ClLandOwner_PK" primary key("ClCode1", "ClCode2", "ClNo", "LandSeq", "OwnerCustUKey");

alter table "ClLandOwner" add constraint "ClLandOwner_ClLand_FK1" foreign key ("ClCode1", "ClCode2", "ClNo") references "ClLand" ("ClCode1", "ClCode2", "ClNo") on delete cascade;

create index "ClLandOwner_Index1" on "ClLandOwner"("ClCode1" asc, "ClCode2" asc, "ClNo" asc);

create index "ClLandOwner_Index2" on "ClLandOwner"("OwnerCustUKey" asc);

comment on table "ClLandOwner" is '擔保品-土地所有權人檔';
comment on column "ClLandOwner"."ClCode1" is '擔保品代號1';
comment on column "ClLandOwner"."ClCode2" is '擔保品代號2';
comment on column "ClLandOwner"."ClNo" is '擔保品編號';
comment on column "ClLandOwner"."LandSeq" is '土地序號';
comment on column "ClLandOwner"."OwnerCustUKey" is '客戶識別碼';
comment on column "ClLandOwner"."OwnerRelCode" is '與授信戶關係';
comment on column "ClLandOwner"."OwnerPart" is '持份比率(分子)';
comment on column "ClLandOwner"."OwnerTotal" is '持份比率(分母)';
comment on column "ClLandOwner"."CreateDate" is '建檔日期時間';
comment on column "ClLandOwner"."CreateEmpNo" is '建檔人員';
comment on column "ClLandOwner"."LastUpdate" is '最後更新日期時間';
comment on column "ClLandOwner"."LastUpdateEmpNo" is '最後更新人員';
