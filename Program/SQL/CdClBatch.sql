drop table "CdClBatch" purge;

create table "CdClBatch" (
  "ApplNo" decimal(7, 0) default 0 not null,
  "GroupNo" varchar2(10),
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "CdClBatch" add constraint "CdClBatch_PK" primary key("ApplNo");

comment on table "CdClBatch" is '擔保品整批匯入批號紀錄檔';
comment on column "CdClBatch"."ApplNo" is '核准號碼';
comment on column "CdClBatch"."GroupNo" is '批號';
comment on column "CdClBatch"."CreateDate" is '建檔日期時間';
comment on column "CdClBatch"."CreateEmpNo" is '建檔人員';
comment on column "CdClBatch"."LastUpdate" is '最後更新日期時間';
comment on column "CdClBatch"."LastUpdateEmpNo" is '最後更新人員';
