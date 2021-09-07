drop table "ClImmRankDetail" purge;

create table "ClImmRankDetail" (
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "SettingSeq" varchar2(1),
  "FirstCreditor" nvarchar2(40),
  "FirstAmt" decimal(16, 2) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ClImmRankDetail" add constraint "ClImmRankDetail_PK" primary key("ClCode1", "ClCode2", "ClNo", "SettingSeq");

comment on table "ClImmRankDetail" is '擔保品不動產檔設定順位明細';
comment on column "ClImmRankDetail"."ClCode1" is '擔保品代號1';
comment on column "ClImmRankDetail"."ClCode2" is '擔保品代號2';
comment on column "ClImmRankDetail"."ClNo" is '擔保品編號';
comment on column "ClImmRankDetail"."SettingSeq" is '設定順位(1~9)';
comment on column "ClImmRankDetail"."FirstCreditor" is '前一順位債權人';
comment on column "ClImmRankDetail"."FirstAmt" is '前一順位金額';
comment on column "ClImmRankDetail"."CreateDate" is '建檔日期時間';
comment on column "ClImmRankDetail"."CreateEmpNo" is '建檔人員';
comment on column "ClImmRankDetail"."LastUpdate" is '最後更新日期時間';
comment on column "ClImmRankDetail"."LastUpdateEmpNo" is '最後更新人員';
