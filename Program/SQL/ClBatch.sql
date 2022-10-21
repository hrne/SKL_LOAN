drop table "ClBatch" purge;

create table "ClBatch" (
  "GroupNo" varchar2(10),
  "Seq" decimal(3, 0) default 0 not null,
  "ApplNo" decimal(7, 0) default 0 not null,
  "EvaCompany" varchar2(2),
  "EvaDate" decimal(8, 0) default 0 not null,
  "ClCode1" decimal(1, 0) default 0 not null,
  "ClCode2" decimal(2, 0) default 0 not null,
  "ClNo" decimal(7, 0) default 0 not null,
  "InsertStatus" decimal(1, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "ClBatch" add constraint "ClBatch_PK" primary key("GroupNo", "Seq");

create index "ClBatch_Index1" on "ClBatch"("ClCode1" asc, "ClCode2" asc, "ClNo" asc);

comment on table "ClBatch" is '擔保品整批匯入檔';
comment on column "ClBatch"."GroupNo" is '批號';
comment on column "ClBatch"."Seq" is '序號';
comment on column "ClBatch"."ApplNo" is '核准號碼';
comment on column "ClBatch"."EvaCompany" is '鑑價公司代碼';
comment on column "ClBatch"."EvaDate" is '鑑價日期';
comment on column "ClBatch"."ClCode1" is '擔保品代號1';
comment on column "ClBatch"."ClCode2" is '擔保品代號2';
comment on column "ClBatch"."ClNo" is '擔保品編號';
comment on column "ClBatch"."InsertStatus" is '寫入狀態';
comment on column "ClBatch"."CreateDate" is '建檔日期時間';
comment on column "ClBatch"."CreateEmpNo" is '建檔人員';
comment on column "ClBatch"."LastUpdate" is '最後更新日期時間';
comment on column "ClBatch"."LastUpdateEmpNo" is '最後更新人員';
