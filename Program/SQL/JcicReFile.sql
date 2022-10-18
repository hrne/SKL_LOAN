drop table "JcicReFile" purge;

create table "JcicReFile" (
  "SubmitKey" varchar2(3),
  "JcicDate" decimal(8, 0) default 0 not null,
  "ReportTotal" decimal(8, 0) default 0 not null,
  "CorrectCount" decimal(8, 0) default 0 not null,
  "MistakeCount" decimal(8, 0) default 0 not null,
  "NoBackFileCount" decimal(8, 0) default 0 not null,
  "NoBackFileDate" decimal(8, 0) default 0 not null,
  "CreateDate" timestamp,
  "CreateEmpNo" varchar2(6),
  "LastUpdate" timestamp,
  "LastUpdateEmpNo" varchar2(6)
);

alter table "JcicReFile" add constraint "JcicReFile_PK" primary key("SubmitKey", "JcicDate");

create index "JcicReFile_Index1" on "JcicReFile"("SubmitKey" asc);

create index "JcicReFile_Index2" on "JcicReFile"("JcicDate" asc);

comment on table "JcicReFile" is '聯徵回寫紀錄檔';
comment on column "JcicReFile"."SubmitKey" is '報送單位代號';
comment on column "JcicReFile"."JcicDate" is '報送日期';
comment on column "JcicReFile"."ReportTotal" is '報送總筆數';
comment on column "JcicReFile"."CorrectCount" is '正確筆數';
comment on column "JcicReFile"."MistakeCount" is '錯誤筆數';
comment on column "JcicReFile"."NoBackFileCount" is '未回檔筆數';
comment on column "JcicReFile"."NoBackFileDate" is '未回檔日期';
comment on column "JcicReFile"."CreateDate" is '建檔日期時間';
comment on column "JcicReFile"."CreateEmpNo" is '建檔人員';
comment on column "JcicReFile"."LastUpdate" is '最後更新日期時間';
comment on column "JcicReFile"."LastUpdateEmpNo" is '最後更新人員';
